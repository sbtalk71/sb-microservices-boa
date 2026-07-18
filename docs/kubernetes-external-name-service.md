Below is a simple Kubernetes example showing how a pod can access the public JSONPlaceholder API (`https://jsonplaceholder.typicode.com`) using a Service of type **ExternalName**.

## Architecture

```text
+-------------------+
| BusyBox Pod       |
|                   |
| curl http://      |
| json-api/posts/1  |
+---------+---------+
          |
          v
+-------------------+
| Service           |
| Type: ExternalName|
| Name: json-api    |
+---------+---------+
          |
          v
jsonplaceholder.typicode.com
          |
          v
https://jsonplaceholder.typicode.com/posts/1
```

---

## 1. Create ExternalName Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: json-api
spec:
  type: ExternalName
  externalName: jsonplaceholder.typicode.com
```

Apply:

```bash
kubectl apply -f externalname-service.yml
```

Verify:

```bash
kubectl get svc json-api
```

Expected:

```text
NAME       TYPE           CLUSTER-IP   EXTERNAL-IP                     PORT(S)
json-api   ExternalName   <none>       jsonplaceholder.typicode.com    <none>
```

---

## 2. Create BusyBox Pod

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: busybox
spec:
  containers:
  - name: busybox
    image: busybox:1.36
    command:
      - sleep
      - "3600"
```

Apply:

```bash
kubectl apply -f busybox.yml
```

Wait until running:

```bash
kubectl get pods
```

---

## 3. Verify DNS Resolution

Open shell:

```bash
kubectl exec -it busybox -- sh
```

Inside pod:

```sh
nslookup json-api
```

Expected output:

```text
json-api.default.svc.cluster.local
canonical name = jsonplaceholder.typicode.com
```

This proves Kubernetes DNS is resolving the service name to the external hostname.

---

## 4. Call the API

BusyBox does not always contain curl. Use wget:

```sh
wget -qO- http://json-api/posts/1
```

Expected:

```json
{
  "userId": 1,
  "id": 1,
  "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
  "body": "quia et suscipit..."
}
```

---

## 5. Using Curl Instead

If you prefer curl:

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: curl-client
spec:
  containers:
  - name: curl
    image: curlimages/curl:latest
    command:
      - sleep
      - "3600"
```

Execute:

```bash
kubectl exec -it curl-client -- sh
```

Then:

```sh
curl http://json-api/posts/1
```

---

## Important HTTPS Note

`ExternalName` performs only DNS aliasing. It does **not** provide TLS termination or protocol conversion.

Since JSONPlaceholder redirects HTTP to HTTPS, a better call is:

```sh
curl https://json-api/posts/1
```

or

```sh
wget -qO- https://json-api/posts/1
```

---

## Check the DNS Entry Created by Kubernetes

From any pod:

```sh
nslookup json-api.default.svc.cluster.local
```

Result:

```text
json-api.default.svc.cluster.local
    CNAME jsonplaceholder.typicode.com
```

This demonstrates the key purpose of **ExternalName**: Kubernetes creates a DNS CNAME record so workloads can use an internal service name (`json-api`) while actually communicating with an external system.

### Typical Real-World Uses

* Database running outside Kubernetes
* Legacy application on a VM
* External REST APIs
* SaaS services
* Cloud-managed databases such as:

  * Amazon Web Services RDS
  * Google Cloud Cloud SQL
  * Microsoft Azure SQL Database

The application code simply uses:

```text
http://json-api/posts/1
```

instead of:

```text
https://jsonplaceholder.typicode.com/posts/1
```

making external dependencies easier to abstract and replace later.
