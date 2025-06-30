# Code Health Roadmap

## 1. Refactor Service Layer for Maintainability
- [ ] Refactor long and repetitive update methods in all service implementations (e.g., `DonationServiceImpl`, `ResourceRequestServiceImpl`, `ResourceServiceImpl`, `OrphanageServiceImpl`) using helper methods for field updates and validation.
- [ ] Centralize common validation and update logic to reduce code duplication.

## 2. Complete and Harden Security Logic
- [ ] Implement all missing logic in `SecurityServiceImpl` (replace `TODO` comments with real code).
- [ ] Standardize and enforce permission checks using Spring Security context.
- [ ] Ensure all security-related exceptions are logged and handled appropriately.

## 3. Standardize Exception Handling
- [ ] Replace generic exceptions with custom exceptions (`ResourceNotFoundException`, `ValidationException`, `PermissionDeniedException`, `DuplicateResourceException`) throughout the service and controller layers.
- [ ] Ensure all exceptions are logged with context, especially in security and audit-related code.

## 4. Enforce Input Validation
- [ ] Add validation annotations (e.g., `@NotNull`, `@Size`, `@Email`) to all DTOs.
- [ ] Add explicit validation checks in service methods for business rules and security.

## 5. Expand and Improve Test Coverage
- [ ] Add/expand unit and integration tests for all update methods, security checks, and exception handling.
- [ ] Ensure all new and refactored code is covered by tests, including edge cases.

## 6. Audit and Enhance Logging
- [ ] Ensure all critical actions and exceptions are logged using SLF4J.
- [ ] Avoid logging sensitive information (e.g., passwords, tokens).

## 7. Documentation and Best Practices
- [ ] Update documentation to reflect new patterns for validation, exception handling, and security.
- [ ] Share best practices with the team to ensure consistency in future development.

---

**How to Use This Roadmap:**
- Tackle items in order of risk and impact (start with security and service refactoring).
- Assign tasks to team members and track progress.
- Review and test after each major change to ensure stability and maintainability. 