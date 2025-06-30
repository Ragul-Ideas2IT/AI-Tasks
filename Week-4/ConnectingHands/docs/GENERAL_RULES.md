# General Project Rules

## Version Control

### Branch Naming Convention
- **Feature Branches**: `feature/[ticket-id]-short-description`
  - Example: `feature/BE-101-user-authentication`
  - Example: `feature/FE-102-donation-form`
- **Bug Fix Branches**: `fix/[ticket-id]-short-description`
  - Example: `fix/BE-103-login-validation`
- **Hotfix Branches**: `hotfix/[ticket-id]-short-description`
  - Example: `hotfix/BE-104-security-patch`
- **Release Branches**: `release/v[version]`
  - Example: `release/v1.0.0`

### Commit Messages
Follow Conventional Commits format:
```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or modifying tests
- `chore`: Maintenance tasks

Examples:
```
feat(auth): implement JWT authentication
fix(api): correct user registration validation
docs(readme): update installation instructions
style(ui): format donation form layout
```

### Pull Request Guidelines
1. **Title Format**: `[Ticket ID] Brief description`
2. **Description Template**:
   ```markdown
   ## Description
   [Detailed description of changes]

   ## Related Tickets
   - [Ticket ID]: [Brief description]

   ## Type of Change
   - [ ] Bug fix
   - [ ] New feature
   - [ ] Breaking change
   - [ ] Documentation update

   ## Testing
   - [ ] Unit tests added/updated
   - [ ] Integration tests added/updated
   - [ ] Manual testing completed

   ## Checklist
   - [ ] Code follows project style
   - [ ] Documentation updated
   - [ ] Tests added/updated
   - [ ] All tests passing
   ```

## Coding Style

### General Principles
1. **Code Organization**
   - Follow project structure guidelines
   - Keep files focused and single-responsibility
   - Use meaningful file and directory names
   - Group related functionality

2. **Naming Conventions**
   - Use descriptive, intention-revealing names
   - Follow language-specific conventions
   - Be consistent across the codebase
   - Avoid abbreviations unless widely understood

3. **Documentation**
   - Document public APIs and interfaces
   - Use JSDoc/TSDoc for JavaScript/TypeScript
   - Use JavaDoc for Java
   - Keep documentation up-to-date
   - Document complex algorithms and business logic

### Frontend Specific
1. **Component Structure**
   - One component per file
   - Clear component hierarchy
   - Proper prop typing
   - Consistent component naming

2. **State Management**
   - Clear state organization
   - Proper state immutability
   - Efficient state updates
   - Clear state access patterns

3. **Styling**
   - Follow project's Prettier configuration
   - Use consistent CSS/SCSS patterns
   - Follow BEM naming convention
   - Maintain responsive design

### Backend Specific
1. **API Design**
   - Follow REST principles
   - Consistent endpoint naming
   - Proper HTTP method usage
   - Clear request/response structures

2. **Database**
   - Use proper indexing
   - Optimize queries
   - Follow naming conventions
   - Maintain data integrity

3. **Error Handling**
   - Consistent error responses
   - Proper error logging
   - Clear error messages
   - Proper exception handling

## Security

### Input Validation
1. **Frontend**
   - Validate all user inputs
   - Implement proper form validation
   - Sanitize data before submission
   - Use proper input types

2. **Backend**
   - Validate all incoming requests
   - Sanitize all user inputs
   - Use parameterized queries
   - Implement proper validation layers

### Authentication & Authorization
1. **User Authentication**
   - Secure password storage
   - Proper session management
   - Secure token handling
   - Implement proper logout

2. **Access Control**
   - Role-based access control
   - Resource ownership validation
   - Proper permission checks
   - Secure API endpoints

### Data Protection
1. **Sensitive Data**
   - Encrypt sensitive data
   - Secure data transmission
   - Proper data storage
   - Data retention policies

2. **API Security**
   - CSRF protection
   - XSS prevention
   - SQL injection prevention
   - Rate limiting

### Security Best Practices
1. **Code Security**
   - Follow OWASP Top 10
   - Regular security audits
   - Dependency updates
   - Security testing

2. **Configuration**
   - Secure environment variables
   - Proper secret management
   - Secure configuration
   - Regular security reviews

3. **Monitoring**
   - Security logging
   - Intrusion detection
   - Regular audits
   - Incident response

## Development Workflow

### Local Development
1. **Setup**
   - Follow setup instructions
   - Use correct Node.js version
   - Install required dependencies
   - Configure environment

2. **Development Process**
   - Create feature branch
   - Follow coding standards
   - Write tests
   - Regular commits

3. **Testing**
   - Run tests locally
   - Check code coverage
   - Manual testing
   - Cross-browser testing

### Code Review
1. **Review Process**
   - Code quality check
   - Security review
   - Performance review
   - Documentation check

2. **Review Guidelines**
   - Constructive feedback
   - Clear explanations
   - Follow checklist
   - Timely responses

### Deployment
1. **Pre-deployment**
   - All tests passing
   - Code review completed
   - Documentation updated
   - Security checks passed

2. **Deployment Process**
   - Follow deployment checklist
   - Monitor deployment
   - Verify functionality
   - Update documentation

## Maintenance

### Code Maintenance
1. **Regular Updates**
   - Dependency updates
   - Security patches
   - Performance optimization
   - Code refactoring

2. **Documentation**
   - Keep documentation current
   - Update API documentation
   - Maintain changelog
   - Update setup instructions

### Monitoring
1. **Performance**
   - Monitor application performance
   - Track error rates
   - Monitor resource usage
   - Performance optimization

2. **Security**
   - Security monitoring
   - Vulnerability scanning
   - Access logging
   - Incident response 