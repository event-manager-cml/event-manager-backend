# Event Manager Backend

This **Spring Boot** application provides a RESTful API to manage event data, including batch processing of multiple event records. It allows CRUD operations on events and processes batches of events asynchronously using an executor service.

## Features

- **CRUD Operations**: Create, read, update, and delete events.
- **Batch Processing**: Supports batch processing of multiple event records in parallel.
- **Exception Handling**: Global exception handling for better error reporting.
- **Data Validation**: Validates input data using the @Valid and @NotBlank annotations.
- **Asynchronous Processing**: Batch processing is done in parallel using CompletableFuture.

## Technologies Used

- Spring Boot 3.x
- Java 17
- PostgreSQL
- Hibernate (JPA)
- Lombok for reducing boilerplate code
- ExecutorService for asynchronous processing
- REST API
- JUnit/MockMVC (optional for testing)

## Prerequisites

- Java 17 or higher
- PostgreSQL (Database configuration in application.properties)
- Maven (for building the project)

## Installation

### 1. Unzip and navigate to the project directory:

```bash
cd event-manager-backend
```

### 2. Configure the PostgreSQL database in the src/main/resources/application.properties file:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build the project using Maven:

```
mvn clean install
```

### 4. Run the Spring Boot application:

```
mvn spring-boot:run
```

## API Endpoints

### Event Endpoints

- **GET** `/api/events`: Retrieve all events.
- **GET** `/api/events/{eventId}`: Retrieve a single event by its ID.
- **POST** `/api/events`: Create a new event.
- **PUT** `/api/events/{eventId}`: Update an existing event by ID.
- **DELETE** `/api/events/{eventId}`: Delete an event by ID.
  Batch Processing Endpoint
- **POST** `/api/events/batch`: Process a batch of events asynchronously.

Request Body Example for Batch Processing:

```json
{
  "batchId": "4d53d2d1-56b2-4c2a-a417-5fdb832f2310",
  "records": [
    {
      "transId": "12345",
      "transTms": "2024-08-09T10:15:30",
      "rcNum": "54321",
      "clientId": "client123",
      "event": [
        {
          "eventCnt": 1,
          "locationCd": "loc123",
          "locationId1": "id123",
          "locationId2": "id456",
          "addrNbr": "789"
        }
      ]
    }
  ]
}
```

## Exception Handling

- **EventNotFoundException**: Thrown when an event is not found (HTTP 404).
- **BatchProcessingException**: Thrown when there is an issue processing the batch (HTTP 500).
- **Validation Errors**: Handled using MethodArgumentNotValidException (HTTP 400).

## Testing

You can run the unit tests using the following command:

```bash
mvn test
```

## Shutdown Handling

The application uses an ExecutorService for asynchronous tasks. It ensures graceful shutdown using the @PreDestroy annotation in the EventService class.

## Postman Link

https://learning-platform-3662.postman.co/workspace/Event-Manager~da2a2616-eb8e-4a91-a4ef-1d01382e99b2/collection/38001409-7cc92303-3312-4aab-b8e9-e2ca00772185?action=share&creator=38001409