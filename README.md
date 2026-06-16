Here is a comprehensive `README.md` file designed for this project. It includes a specific preface detailing the objectives of the Lecture 2.9 in-class exercise, along with a technical explanation of the architectural changes implemented in this "hardened" starter codebase.

***

## CSD230 Lecture 2.9: Building RESTful Services

> Starter code for [Lecture 2.9...](https://docs.google.com/document/d/1wcGJcMPuk1tBNg2wUEo2_Sfym36FosdQxFs7CUV_oUM/edit?usp=sharing)

## Preface: The Shift to "Headless" Architecture

In previous lectures and labs, this application operated as a monolithic system, where the Java backend generated HTML pages via Thymeleaf and served them directly to the client.

With Lecture 2.9, we transition to a **decoupled, "headless" architecture**. The backend is refactored to serve strictly as an API, returning raw data in JSON format rather than fully rendered user interfaces. This decoupling enables the backend to serve multiple clients, such as modern single-page React applications, mobile applications, or external developer tools, without forcing a visual layer upon them.

---

## Changes to the Starter Code ("Hardened" State)

To support this transition safely, several structural changes have been made to the starter code relative to earlier monolithic designs. These upgrades ensure the API can handle real-world payload variations and test scenarios without system failure:

### 1. Object Wrappers over Primitives
In standard monolithic forms, form submission often ensures fields are initialized. In REST APIs, however, client JSON requests may omit optional fields entirely.
* **The Change:** Within `PublicationEntity` and `TicketEntity`, variables like `price` and `copies` have been updated from primitive types (`double`, `int`) to object wrappers (`Double`, `Integer`).
* **The Reason:** If a client submits a payload missing a price, Jackson (Spring's JSON deserializer) maps the field to `null`. If a primitive were used, this mapping would trigger a serialization exception and crash the request. Using wrappers allows the system to store `null` gracefully in the database or handle it via validation logic.

### 2. Tailored Security Configuration
Modern web security parameters require distinct rules for state-altering API calls compared to standard browser forms.
* **The Change:** `WebSecurityConfig` has been modified to explicitly exclude path patterns matching `/api/rest/**` from CSRF protection:
  ```java
  .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/rest/**", "/logout"))
  ```
* **The Reason:** CSRF (Cross-Site Request Forgery) protection relies on session-bound tokens, which stateless client tools like Postman, cURL, or React cannot easily obtain upfront. Bypassing CSRF on `/api/rest/**` paths ensures developer tools and decoupled clients can submit state-changing operations (POST, PUT, DELETE) immediately, while keeping security policies strict for non-API endpoints.

### 3. Automatic Data Seeding
* **The Change:** In `Application.java`, the `CommandLineRunner` bean incorporates `JavaFaker` to generate mock data automatically upon startup when the database is empty:
  ```java
  faker.book().title();
  faker.book().author();
  ```
* **The Reason:** This ensures that as soon as the application starts (particularly under the H2 memory-based profile), developers have realistic, populated data sets to test the read (GET) endpoints without needing to manually insert SQL scripts.

---

## Project Structure & Architecture

```text
├── src/main/java/app/
│   ├── config/
│   │   └── WebSecurityConfig.java             # Configured for stateless REST and H2 access
│   ├── controllers/
│   │   ├── BookRestController.java            # The REST controller returning JSON
│   │   ├── BookNotFoundException.java         # Custom runtime exception
│   │   └── BookNotFoundAdvice.java            # @RestControllerAdvice interceptor for 404s
│   ├── entities/
│   │   └── ProductEntity.java                 # Single-table inheritance base class
│   └── repositories/
│       └── BookRepository.java                # JPA Data Access
```

1. **The Model Layer:** Utilizes single-table inheritance mapping via `ProductEntity` down to specialized item schemas (`BookEntity`, `TicketEntity`, etc.).
2. **The Global Error Interceptor:** Implementations of `@RestControllerAdvice` intercept custom logic failures (like resource-not-found exceptions) and translate them into standardized JSON error bodies accompanied by correct HTTP status headers (e.g., `404 Not Found`).
3. **The Presentation/Controller Layer:** Marked by `@RestController` which combines `@Controller` and `@ResponseBody` to instruct Spring to serialize Java return statements into JSON strings automatically via the Jackson engine.

---

## Local Development Setup

### Prerequisites
* Java Development Kit (JDK) 25
* Maven 3.x
* Git Bash (recommended shell for Windows environments to run `curl` commands smoothly)

### Profiles Configuration
The application supports both local in-memory H2 development and Dockerized MySQL. The configuration profile is toggled in `src/main/resources/application.properties`:

```properties
# Toggle active profile:
spring.profiles.active=h2
# spring.profiles.active=mysql
```

* **H2 Profile:** Uses an in-memory database (`jdbc:h2:mem:csd230`), requiring zero local setup.
* **MySQL Profile:** Connects to port `3307` mapped from the root `docker-compose.yml` configuration. If using this profile, make sure to launch the local container:
  ```bash
  docker compose up -d
  ```

---

## Testing and API Interactivity

Once the application is running, the following internal endpoints and tools are accessible:

* **Landing Interface:** [http://localhost:8080/](http://localhost:8080/) (requires authentication with `admin`/`admin` or `user`/`user`)
* **Interactive OpenAPI/Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **In-Memory H2 Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:csd230`, Username: `sa`, Password: *blank*)

### Standard Test Commands (Git Bash / POSIX Terminal)

Using Git Bash in your terminal prevents parsing issues associated with nested quotes in standard Windows PowerShell environments.

#### A. Fetch Catalog
```bash
curl -X GET http://localhost:8080/api/rest/books
```

#### B. Insert a New Book (Note: Missing price parameter is tolerated)
```bash
curl -X POST http://localhost:8080/api/rest/books \
     -H "Content-Type: application/json" \
     -d '{"title":"Command Line Magic","author":"Terminal User","copies":10}'
```

#### C. Modify an Existing Book (ID: 1)
```bash
curl -X PUT http://localhost:8080/api/rest/books/1 \
     -H "Content-Type: application/json" \
     -d '{"title":"Updated via Curl","author":"Fred Carella","price":55.00,"copies":25}'
```

#### D. Delete a Book
```bash
curl -X DELETE http://localhost:8080/api/rest/books/1
```

#### E. Test Exception Handler Response (Expecting 404 JSON response body)
```bash
curl -i http://localhost:8080/api/rest/books/9999
```