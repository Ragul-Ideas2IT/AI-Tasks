# Collaborative Document System - Development Context

## Architecture Overview
- Microservices: user-service, document-service, collaboration-service
- Communication: REST APIs, WebSocket for real-time, Redis for events
- Data: PostgreSQL for persistence, Redis for caching/messaging
- Tech Stack: Java 17+, Spring Boot 3, Spring Cloud

## Code Standards
- Spring Boot for all backend services
- Async patterns for real-time and event-driven code
- DTOs for API contracts
- Exception handling with custom error responses
- OpenAPI documentation for all endpoints

## Integration Patterns
- Services communicate via REST and Redis events
- All services expose health check endpoints
- Authentication via JWT tokens
- Centralized logging and tracing
- Docker containerization for deployment

## Service Structure
Each service follows this pattern:
- src/main/java: Main application code
- src/main/resources: Configurations
- controllers/: REST/WebSocket endpoints
- services/: Business logic
- models/: Entities and DTOs
- repositories/: Data access
- config/: Service configuration
- tests/: Unit and integration tests
