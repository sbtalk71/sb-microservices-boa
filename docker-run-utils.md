# Some Important Docker image run commands
### Run Redis Server with volume
```sh
docker run --rm --name redis -p 6379:6379 -v redis-data:/data -d redis
```
### Stop the above Redis Server
```sh
docker container stop redis
```
