# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.7/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.7/maven-plugin/build-image.html)
* [Spring Data for Apache Cassandra](https://docs.spring.io/spring-boot/3.5.7/reference/data/nosql.html#data.nosql.cassandra)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.7/reference/web/servlet.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.7/reference/using/devtools.html)

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

- [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
- [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.7/maven-plugin)
- [Create an OCI image](https://docs.spring.io/spring-boot/3.5.7/maven-plugin/build-image.html)
- [Spring Data for Apache Cassandra](https://docs.spring.io/spring-boot/3.5.7/reference/data/nosql.html#data.nosql.cassandra)
- [Spring Web](https://docs.spring.io/spring-boot/3.5.7/reference/web/servlet.html)
- [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.7/reference/using/devtools.html)

### Guides
The following guides illustrate how to use some features concretely:

- [Spring Data for Apache Cassandra](https://spring.io/guides/gs/accessing-data-cassandra/)
- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

## Local development and Docker

This project uses Apache Cassandra (via Docker) during development. The repository includes a `docker/docker-compose.yml` to start Cassandra quickly.

### Java version

This project is configured to build with Java 25 (see `pom.xml` property `java.version`). Make sure you have a compatible JDK installed when building or running locally. If you prefer another JDK, update the `pom.xml` and test locally.

### Start Cassandra with Docker Compose

From the project root (PowerShell):

```powershell
docker-compose -f .\docker\docker-compose.yml up -d
```

Check containers:

```powershell
docker ps
```

The Cassandra container in the included compose may have the name `cassandra-db` (see `docker/info.txt`). Adjust the container name below if yours is different.

### Connect to Cassandra using cqlsh

Run:

```powershell
docker exec -it cassandra-db cqlsh
```

Create the keyspace (example from `docker/info.txt`):

```sql
CREATE KEYSPACE IF NOT EXISTS mykeyspace 
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE mykeyspace;
```

### Application configuration: domain

The application includes an `app.domain` property in `src/main/resources/application.properties`. By default it is set to:

```
app.domain=http://localhost:8080
```

This value is used to compose the public shortened URL (e.g. `http://localhost:8080/api/url/{hash}`). Change it per environment (dev/prod) as needed.

### Run the application

From PowerShell (project root) use the included Maven wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

Or build the JAR and run it:

```powershell
.\mvnw.cmd clean package
java -jar target\url-0.0.1-SNAPSHOT.jar
```

### Quick API tests

Shorten a URL (PowerShell):

```powershell
# using curl.exe to avoid PowerShell alias differences
curl.exe -X POST -H "Content-Type: text/plain" -d "https://example.com" http://localhost:8080/api/url/shorten
```

You should receive a JSON response containing `shortenedUrl` with the full shortened link, for example:

```json
{"shortenedUrl":"http://localhost:8080/api/url/abc1234"}
```

Open the shortened link in your browser (or via curl) to be redirected:

```powershell
# example
curl.exe -v http://localhost:8080/api/url/abc1234
```

If the redirect does not work, check the logs and ensure the stored long URL is a full absolute URL (including scheme `http://` or `https://`).

### Notes and troubleshooting

- If you see URLs saved with extra double-quotes, the service strips surrounding quotes before saving. Ensure your client sends the raw URL (plain text) in the POST body.
- If you use a different Cassandra container name, replace `cassandra-db` in the `docker exec` command.
- To change the port, update `app.domain` and any local port mappings in `docker-compose.yml`.

### API

* Para ober a url curta

curl --location 'http://localhost:8080/api/url/shorten' \
--header 'Content-Type: application/json' \
--data '"sua-url-longa"'

* Retorna json

{
    "shortenedUrl": "http://localhost:8080/api/url/KQNKaPx"
}

* Para redirecionar a sua-url-longa

curl --location 'http://localhost:8080/api/url/KQNKaPx'
