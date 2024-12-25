# Drone Dispatch Service

This project provides a REST API to manage a fleet of drones for transporting medications. The service enables clients to perform operations such as registering drones, loading medications, checking loaded items, querying available drones, and monitoring battery levels.

---

## Features

The application implements the following APIs:

1. **Register a Drone**
    - Adds a new drone to the system with details such as serial number, model, weight limit, and initial battery capacity.

2. **Load a Drone with Medications**
    - Allows loading a drone with medication items while ensuring:
        - The total weight does not exceed the drone's weight limit.
        - The battery level is above 25% when in the `LOADING` state.

3. **Check Loaded Medication Items**
    - Retrieves the list of medications loaded on a specific drone.

4. **Check Available Drones for Loading**
    - Lists all drones available for loading.

5. **Check Drone Battery Level**
    - Provides the current battery level of a specific drone.

---

## Prerequisites

- Java 17
- Maven 3.8+
- Docker Desktop

---

## Steps to Build and Run the Application

### 1. Build the Application

Navigate to project folder then Generate the JAR file by running the following Maven command:

```bash
mvn clean install
```

This will create a `target/DroneBackEnd.jar` file.

### 2. Build the Docker Image

Build the Docker image for the application using the following command:

```bash
docker build -t spring-boot-elmenus .
```

### 3. Run the Docker Container

Run the application in a Docker container with the following command:

```bash
docker run -d -p 8080:8080 --name my-app-container spring-boot-elmenus
```

The application will now be accessible at `http://localhost:8080`.

---

## Postman Collection

In the repository, you can find a folder named `collection` containing a pre-configured **Postman Collection**. Use this collection to test the APIs directly.

---

## Additional Notes

- The service uses an in-memory database to preload required data.
- A periodic task monitors the battery levels of drones and logs events for audit purposes.
- The application strictly ensures that drones are not overloaded or used in an invalid state.
- JUnit tests are included to verify the functionality of the APIs.

---

