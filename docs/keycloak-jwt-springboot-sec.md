## Spring Boot WebFlux JWT with Keycloak

### Setup KeyCloak
### Run KeyCloack
```sh
docker run -d -p 8080:8080 --name my-keycloak -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin -v keycloak_data:/opt/keycloak/data docker.io/keycloak/keycloak:latest start-dev
```
**2. Realm setup — two options**

**Option A: Admin console (quickest to see what's happening)**

1. Log in at `http://localhost:8080` → admin / admin
2. Top-left dropdown → **Create realm** → name it `myrealm`
3. **Clients** → **Create client**
   - Client ID: `my-client`
   - Client type: OpenID Connect
   - Client authentication: **ON** if it's a confidential client (server-side); **OFF** if public (SPA/mobile). For the `curl` password-grant example, turn it **ON** and grab the client secret from the **Credentials** tab.
   - Under **Capability config**, enable **Direct access grants** (needed for the password-grant curl test)
4. **Realm roles** → **Create role** → create `admin` and `user`
5. **Users** → **Create user** → username `alice`
   - **Credentials** tab → set password `secret`, toggle **Temporary** OFF
   - **Role mapping** tab → **Assign role** → assign `admin` (this lands in `realm_access.roles`, matching the converter from before)

**Option B: Realm export JSON (repeatable, good for CI/local resets)**

Save as `myrealm-realm.json`:
```json
{
  "realm": "myrealm",
  "enabled": true,
  "sslRequired": "none",
  "roles": {
    "realm": [
      { "name": "admin" },
      { "name": "user" }
    ]
  },
  "clients": [
    {
      "clientId": "my-client",
      "enabled": true,
      "publicClient": false,
      "secret": "my-client-secret",
      "directAccessGrantsEnabled": true,
      "standardFlowEnabled": true,
      "redirectUris": ["*"],
      "webOrigins": ["*"]
    }
  ],
  "users": [
    {
      "username": "alice",
      "enabled": true,
      "email": "alice@example.com",
      "credentials": [
        { "type": "password", "value": "secret", "temporary": false }
      ],
      "realmRoles": ["admin"]
    }
  ]
}
```
## Spring Boot Setup


**1. Dependencies (pom.xml)**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**2. application.yml**
```yaml
server:
  port: 8081

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/myrealm
          jwk-set-uri: http://localhost:8080/realms/myrealm/protocol/openid-connect/certs
```

`issuer-uri` alone is usually enough — Spring auto-discovers the JWK set from the realm's OIDC config. `jwk-set-uri` is a fallback if discovery fails (e.g. Keycloak behind a different internal hostname than the token's `iss` claim).

**3. Security config — reactive style**
```java
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/actuator/health", "/public/**").permitAll()
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesConverter()))
            );
        return http.build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
```

**4. Extracting Keycloak's realm_access.roles**

Keycloak nests roles under `realm_access.roles`, not the standard `scope` claim, so Spring's default converter won't see them — you need a custom one:

```java
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.get("roles") == null) {
            return List.of();
        }
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) realmAccess.get("roles");

        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            .collect(Collectors.toList());
    }
}
```

**5. A protected reactive endpoint**
```java
@RestController
public class DemoController {

    @GetMapping("/api/me")
    public Mono<Map<String, Object>> me(@AuthenticationPrincipal Jwt jwt) {
        return Mono.just(Map.of(
            "username", jwt.getClaimAsString("preferred_username"),
            "email", jwt.getClaimAsString("email"),
            "roles", jwt.getClaim("realm_access")
        ));
    }

    @GetMapping("/admin/dashboard")
    public Mono<String> adminOnly() {
        return Mono.just("Welcome, admin");
    }
}
```

**Testing it**
```bash
# Get a token from Keycloak
curl -X POST http://localhost:8080/realms/myrealm/protocol/openid-connect/token \
  -d "client_id=my-client" \
  -d "username=alice" \
  -d "password=secret" \
  -d "grant_type=password"

# Call the protected endpoint
curl http://localhost:8081/api/me -H "Authorization: Bearer <access_token>"
```
