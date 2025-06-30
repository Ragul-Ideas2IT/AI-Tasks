# Backend Implementation Rules and Standards

## Task Execution

### Task Management
- Work sequentially through `tasks_backend.md` unless dependencies allow parallel work
- Update task status in `tasks_backend.md` with:
  - Completion date
  - Any deviations from original plan
  - Notes on implementation decisions
  - Link to relevant pull requests

### Code Organization
- Follow package structure:
  ```
  com.connectinghands
  ├── config/          # Configuration classes
  ├── controller/      # REST controllers
  ├── dto/            # Data Transfer Objects
  ├── entity/         # JPA entities
  ├── repository/     # JPA repositories
  ├── service/        # Business logic
  ├── security/       # Security related classes
  ├── exception/      # Custom exceptions
  ├── util/           # Utility classes
  └── validation/     # Custom validators
  ```

## Implementation Standards

### API Development
1. **Controller Layer**
   - Use `@RestController` for all API endpoints
   - Implement proper request/response DTOs
   - Use appropriate HTTP methods (GET, POST, PUT, PATCH, DELETE)
   - Follow REST naming conventions
   - Document all endpoints with Swagger annotations

2. **Input Validation**
   - Use Bean Validation annotations (`@Valid`, `@NotNull`, etc.)
   - Implement custom validators where needed
   - Validate all input DTOs
   - Return clear validation error messages

3. **Error Handling**
   - Use global exception handler (`@ControllerAdvice`)
   - Implement custom exceptions for business logic
   - Follow error response format from API spec
   - Log all errors appropriately
   - Never expose internal errors to clients

4. **Authentication & Authorization**
   - Implement JWT-based authentication
   - Use Spring Security for authorization
   - Implement role-based access control
   - Secure all endpoints appropriately
   - Implement rate limiting

### Database Layer

1. **Entity Design**
   - Use JPA annotations properly
   - Implement proper relationships
   - Use appropriate data types
   - Include audit fields (created_at, updated_at)
   - Implement soft delete where appropriate

2. **Repository Layer**
   - Use Spring Data JPA repositories
   - Implement custom queries when needed
   - Use proper indexing
   - Implement pagination for list endpoints
   - Use proper transaction management

3. **Data Access**
   - Use DTOs for data transfer
   - Implement proper mapping between entities and DTOs
   - Use lazy loading appropriately
   - Implement proper caching strategy
   - Handle concurrent access properly

### Testing Standards

1. **Unit Testing**
   - Test all service layer methods
   - Use Mockito for mocking
   - Test both success and failure scenarios
   - Aim for >80% code coverage
   - Test edge cases

2. **Integration Testing**
   - Test all API endpoints
   - Use test containers for database
   - Test complete workflows
   - Test security constraints
   - Test error scenarios

3. **Test Organization**
   - Follow naming convention: `{ClassName}Test`
   - Use descriptive test names
   - Organize tests by feature
   - Use proper test data setup
   - Clean up test data

### Security Standards

1. **Authentication**
   - Implement secure password hashing
   - Use proper JWT configuration
   - Implement token refresh mechanism
   - Handle token expiration
   - Implement proper session management

2. **Authorization**
   - Implement role-based access control
   - Use method-level security
   - Validate user permissions
   - Implement proper resource ownership checks
   - Handle cross-tenant access

3. **Data Protection**
   - Encrypt sensitive data
   - Implement proper logging
   - Handle PII data appropriately
   - Implement audit logging
   - Follow GDPR requirements

### Code Quality

1. **Code Style**
   - Follow Java code conventions
   - Use proper naming conventions
   - Write self-documenting code
   - Use proper comments
   - Keep methods small and focused

2. **Documentation**
   - Document all public APIs
   - Use proper JavaDoc
   - Document complex business logic
   - Keep README updated
   - Document configuration

3. **Performance**
   - Optimize database queries
   - Implement proper caching
   - Use connection pooling
   - Handle large datasets properly
   - Monitor performance metrics

### Deployment

1. **Configuration**
   - Use environment-specific properties
   - Externalize configuration
   - Use proper logging configuration
   - Configure proper security settings
   - Handle secrets properly

2. **Monitoring**
   - Implement health checks
   - Set up proper logging
   - Configure metrics collection
   - Set up alerts
   - Monitor performance

3. **CI/CD**
   - Implement automated builds
   - Run tests automatically
   - Implement code quality checks
   - Automate deployment
   - Version management

## Best Practices

### General
- Follow SOLID principles
- Write clean, maintainable code
- Use proper design patterns
- Handle errors gracefully
- Log appropriately

### Performance
- Optimize database queries
- Use proper indexing
- Implement caching
- Handle concurrent access
- Monitor performance

### Security
- Follow OWASP guidelines
- Implement proper validation
- Handle sensitive data
- Use secure communication
- Regular security audits

### Testing
- Write comprehensive tests
- Use proper test data
- Test edge cases
- Maintain test coverage
- Regular test reviews

## Review Process

### Code Review
- Review for security issues
- Check code quality
- Verify test coverage
- Review documentation
- Check performance impact

### Testing Review
- Verify test coverage
- Check test quality
- Review test data
- Verify edge cases
- Check integration tests

### Security Review
- Check authentication
- Verify authorization
- Review data protection
- Check input validation
- Verify error handling 