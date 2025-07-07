# Collaborative Document System (Java, Spring Boot)

A microservices-based real-time collaborative document editing platform inspired by Google Docs.

## Main Modules
- **user-service**: User authentication, profiles, permissions
- **document-service**: Document CRUD, versioning, sharing
- **collaboration-service**: Real-time editing, WebSocket, operational transformation
- **gateway**: API Gateway, routing, centralized authentication
- **shared**: Common models, utilities
- **config**: Centralized configuration
- **registry**: Service discovery
- **monitoring**: Metrics, dashboards

## Technologies
- Java 17+
- Spring Boot 3
- Spring Cloud (Gateway, Config, Eureka)
- PostgreSQL
- Redis
- WebSocket
- Docker Compose

## Quick Start
1. Clone the repo
2. Run `docker-compose up --build`
3. Access services via the API Gateway

---
See `architecture-design.md` and `system-context.md` for detailed architecture and context.
