## **OpenShift 4.x / CRC commands** using  GitHub repository:

**Repository**

```text
https://github.com/sbtalk71/openshift-app.git
```

OpenShift's `oc new-app` command can automatically detect a Git repository and choose an appropriate Source-to-Image (S2I) or Docker build strategy when possible. You can also explicitly specify the builder image, branch, or context directory. ([Red Hat Customer Portal][1])

---

# 1. Login

## Developer

```bash
oc login -u developer -p developer
```

Create a project

```bash
oc new-project openshift-demo
```

Switch project

```bash
oc project openshift-demo
```

---

## Administrator

```bash
crc console --credentials

oc login -u kubeadmin -p <password>
```

---

# 2. Verify

```bash
oc whoami
oc project
oc status
oc get all
```

---

# 3. Deploy Directly from Git (Auto Detection)

```bash
oc new-app https://github.com/sbtalk71/openshift-app.git
```

View resources

```bash
oc get all
```

Watch build

```bash
oc logs -f bc/openshift-app
```

or

```bash
oc get builds
oc logs -f build/<build-name>
```

---

# 4. Deploy with an Application Name

```bash
oc new-app \
https://github.com/sbtalk71/openshift-app.git \
--name=openshift-app
```

---

# 5. Deploy from a Specific Git Branch

```bash
oc new-app \
https://github.com/sbtalk71/openshift-app.git#main
```

or

```bash
oc new-app \
https://github.com/sbtalk71/openshift-app.git#master
```

---

# 6. Deploy with a Java Builder Image (Spring Boot)

If the repository contains a Maven/Gradle Spring Boot project:

```bash
oc new-app \
openjdk-21~https://github.com/sbtalk71/openshift-app.git \
--name=openshift-app
```

OpenShift creates:

* ImageStream
* BuildConfig
* Deployment
* Service

automatically.

---

# 7. Specify Context Directory

If the application resides inside a subdirectory:

```bash
oc new-app \
https://github.com/sbtalk71/openshift-app.git \
--context-dir=spring-app
```

---

# 8. Expose the Application

```bash
oc expose service/openshift-app
```

Verify

```bash
oc get routes
```

Open

```bash
oc get route openshift-app
```

---

# 9. Watch Deployment

```bash
oc rollout status deployment/openshift-app
```

Pods

```bash
oc get pods -w
```

Logs

```bash
oc logs -f deployment/openshift-app
```

---

# 10. Build Commands

List BuildConfigs

```bash
oc get bc
```

Describe

```bash
oc describe bc openshift-app
```

Trigger another build

```bash
oc start-build openshift-app
```

Follow logs

```bash
oc start-build openshift-app --follow
```

Latest build

```bash
oc get builds
```

---

# 11. Binary Build

Create build configuration

```bash
oc new-build \
--binary \
--name=openshift-app
```

Upload current directory

```bash
oc start-build \
openshift-app \
--from-dir=. \
--follow
```

Upload JAR

```bash
oc start-build \
openshift-app \
--from-file=target/openshift-app.jar \
--follow
```

Upload ZIP

```bash
oc start-build \
openshift-app \
--from-archive=application.zip
```

---

# 12. Docker Strategy Build

Repository contains a Dockerfile:

```bash
oc new-build \
https://github.com/sbtalk71/openshift-app.git \
--strategy=docker
```

or

```bash
oc new-build \
--strategy=docker \
https://github.com/sbtalk71/openshift-app.git \
--name=openshift-app
```

Trigger build

```bash
oc start-build openshift-app --follow
```

---

# 13. Local Dockerfile Build

```bash
oc new-build \
--binary \
--strategy=docker \
--name=openshift-app
```

Build

```bash
oc start-build \
openshift-app \
--from-dir=. \
--follow
```

---

# 14. Deploy Existing Container Image

```bash
oc new-app \
quay.io/myrepo/openshift-app:latest \
--name=openshift-app
```

Expose

```bash
oc expose svc/openshift-app
```

---

# 15. Update Source Code

After committing new changes to GitHub

```bash
oc start-build openshift-app
```

or

```bash
oc start-build openshift-app --follow
```

---

# 16. Scale

```bash
oc scale deployment/openshift-app \
--replicas=3
```

Verify

```bash
oc get pods
```

---

# 17. Rollout

Status

```bash
oc rollout status deployment/openshift-app
```

History

```bash
oc rollout history deployment/openshift-app
```

Restart

```bash
oc rollout restart deployment/openshift-app
```

Undo

```bash
oc rollout undo deployment/openshift-app
```

---

# 18. Logs

```bash
oc logs deployment/openshift-app
```

Follow

```bash
oc logs -f deployment/openshift-app
```

Previous

```bash
oc logs PODNAME --previous
```

---

# 19. Troubleshooting

```bash
oc status

oc get all

oc get pods

oc describe pod POD

oc describe deployment openshift-app

oc get events

oc logs POD

oc logs -f POD
```

---

# 20. Delete the Application

```bash
oc delete all \
-l app=openshift-app
```

Delete BuildConfig

```bash
oc delete bc openshift-app
```

Delete ImageStream

```bash
oc delete is openshift-app
```

Delete Route

```bash
oc delete route openshift-app
```

Delete Project

```bash
oc delete project openshift-demo
```

---

## Complete Demo Flow

```bash
# Login
oc login -u developer -p developer

# Create project
oc new-project openshift-demo

# Deploy from GitHub
oc new-app \
openjdk-21~https://github.com/sbtalk71/openshift-app.git \
--name=openshift-app

# Watch build
oc logs -f bc/openshift-app

# Watch deployment
oc rollout status deployment/openshift-app

# Expose application
oc expose service/openshift-app

# View route
oc get route

# View pods
oc get pods

# Stream logs
oc logs -f deployment/openshift-app

# Scale
oc scale deployment/openshift-app --replicas=2

# Restart
oc rollout restart deployment/openshift-app

# Clean up
oc delete project openshift-demo
```

This sequence demonstrates the complete OpenShift developer workflow: **login → create project → build from GitHub (S2I) → deploy → expose via Route → monitor → scale → redeploy → clean up**, using your repository as the source.

[1]: https://access.redhat.com/node/2389381/chapter-7-builds?utm_source=chatgpt.com "Chapter 5. Creating New Applications - Red Hat Customer Portal"
