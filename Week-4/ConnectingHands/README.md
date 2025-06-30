# Connecting Hands

A platform for connecting orphanages and managing donations.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

## Setup

1. Clone the repository:
```bash
git clone https://github.com/your-org/connecting-hands.git
cd connecting-hands
```

2. Create PostgreSQL database:
```sql
CREATE DATABASE connecting_hands;
```

3. Configure environment variables:
```bash
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export JWT_SECRET=your_256_bit_secret
```

4. Build the project:
```bash
mvn clean install
```

5. Run the application:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080/api/v1`

## API Documentation

Swagger UI is available at `http://localhost:8080/api/v1/swagger-ui.html`

## Development

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── connectinghands/
│   │           ├── config/        # Configuration classes
│   │           ├── controller/    # REST controllers
│   │           ├── dto/          # Data transfer objects
│   │           ├── entity/       # JPA entities
│   │           ├── exception/    # Exception handlers
│   │           ├── repository/   # JPA repositories
│   │           ├── service/      # Business logic
│   │           └── util/         # Utility classes
│   └── resources/
│       ├── application.yml      # Application configuration
│       └── db/
│           └── migration/       # Database migrations
└── test/                        # Test classes
```

### Running Tests
```bash
mvn test
```

## Contributing

1. Create a feature branch
2. Make your changes
3. Run tests
4. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 