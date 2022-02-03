# Practice Webapp BFF

BFF for the Practice Webapp.

## Running Locally (port 8081)

To run the application outside a docker container:

```
./gradlew bootRun -Dspring.profiles.active=local
```

To run the application inside a docker container

```
./docker-composew up --build
```
