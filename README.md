# LocalLegends

A platform for discovering and reviewing local businesses, built with Spring Boot and Java. LocalLegends empowers users to explore, review, and support local businesses, while business owners can showcase their offerings and engage with their communities.

---

## Project Overview

LocalLegends is an open-source backend application that enables users to:
- Find trending local businesses and hidden gems nearby
- Share and read community reviews
- Help businesses grow their reputation through honest feedback

Business owners can:
- Register and manage their business profiles
- Respond to reviews and improve their offerings
- Gain insights into customer feedback

---

## Features

- **User Registration & Authentication**: Secure signup/login with role-based access (Admin, User)
- **Business Management**: Add, update, delete, and list businesses with rich metadata
- **Review & Rating System**: Users can rate and review businesses, with ratings aggregated and displayed
- **Email Notifications**: Welcome emails, new review alerts, and admin notifications
- **Pagination & Filtering**: Find businesses by category, rating, or search terms
- **RESTful API**: Clean and consistent endpoints for all core actions
- **Health Check Endpoint**: Easily verify service uptime

---

## Tech Stack

- **Backend Framework:** Spring Boot (Java)
- **Database:** JPA/Hibernate (RDBMS compatible)
- **Security:** Spring Security (JWT-based authentication)
- **Email Service:** Spring Mail (with Jakarta Mail integration)
- **API Documentation:** Swagger / OpenAPI annotations
- **Other:** Lombok, Validation (Jakarta), Maven

---

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/VIPERtips/locallegends.git
   cd locallegends
   ```

2. **Configure application properties:**
   - Copy `src/main/resources/application.example.properties` to `application.properties`
   - Set your database credentials, mail server settings, and JWT secret.

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   or
   ```bash
   java -jar target/locallegends-*.jar
   ```

---

## Usage Examples

- **Check health:**
  ```
  GET /health
  ```
  Response: `"I'm alive"`

- **Register a new business (Authenticated):**
  ```
  POST /api/businesses
  {
    "name": "Coffee Haven",
    "description": "Best coffee in town",
    "location": "Central Plaza",
    "category": "Cafe",
    ...
  }
  ```

- **Add a review (Authenticated):**
  ```
  POST /api/reviews/{businessId}
  {
    "rating": 5,
    "comment": "Amazing service and coffee!"
  }
  ```

- **Get reviews for a business:**
  ```
  GET /api/reviews/business/{businessId}?page=0&size=10
  ```

---

## API Reference

### Health

- **GET `/health`**  
  Returns a simple status message.

### Businesses

- **POST `/api/businesses`**  
  Create a new business (requires authentication).

- **GET `/api/businesses/{id}`**  
  Get business details by ID.

- **PUT `/api/businesses/{id}`**  
  Update business (requires owner/admin).

- **DELETE `/api/businesses/{id}`**  
  Delete business (requires owner/admin).

- **GET `/api/businesses`**  
  List businesses with optional pagination and filters.

### Reviews

- **POST `/api/reviews/{businessId}`**  
  Add a review to a business (requires authentication).

- **GET `/api/reviews/business/{businessId}`**  
  List reviews for a business (pagination supported).

- **DELETE `/api/reviews/{reviewId}`**  
  Delete a review (requires owner/admin).

### Authentication

- Endpoints for login/registration are provided, typically at `/api/auth/**` (see codebase for details).

---

## License

This project is open source. Please add a `LICENSE` file to specify your chosen license.

---

## Contributing

Contributions are welcome! Please open issues and pull requests on [GitHub](https://github.com/VIPERtips/locallegends).

---

## Further Reading

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JWT Authentication with Spring Security](https://www.baeldung.com/spring-security-oauth-jwt)
- [Jakarta Mail](https://eclipse-ee4j.github.io/mail/)

---

