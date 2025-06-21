# Task 3: Legacy Code Modernization

## Objective
The goal of this task was to identify a piece of "legacy" code within the existing codebase, analyze it for modernization opportunities, and refactor it according to modern best practices. The chosen subject was the `register` method in `AuthServiceImpl.java`.

## 1. Legacy Code Selection
The `register` method was selected because, while functional, it exhibited several anti-patterns and areas for improvement common in older codebases. It handled the critical function of user creation, making its robustness and maintainability essential.

**Original Code:**
```java
@Override
public String register(RegisterDto registerDto) {

    // add check for email exists in database
    if(userRepository.existsByEmail(registerDto.getEmail())){
        throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
    }

    User user = new User();
    user.setFirstName(registerDto.getFirstName());
    user.setLastName(registerDto.getLastName());
    user.setEmail(registerDto.getEmail());
    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName("ROLE_USER").get();
    roles.add(userRole);
    user.setRoles(roles);

    userRepository.save(user);

    return "User registered successfully!.";
}
```

## 2. Analysis of Legacy Code
The initial analysis identified the following issues:

- **No Input Validation:** The `registerDto` was accepted without validating its contents, making the system vulnerable to null pointers, invalid email formats, or empty passwords.
- **Unsafe `Optional` Handling:** The use of `.get()` on the `Optional` returned by `roleRepository.findByName()` was a critical flaw. If the `ROLE_USER` was not found, it would throw an unhandled `NoSuchElementException`.
- **Manual DTO-to-Entity Mapping:** The fields were copied manually, which is verbose and error-prone.
- **Lack of Transactional Integrity:** The sequence of database operations (checking for email, finding a role, saving a user) was not wrapped in a transaction, meaning a failure at a later step would not roll back earlier steps.
- **Hardcoded Role String:** The string `"ROLE_USER"` was hardcoded, making it difficult to change and prone to typos.

## 3. Modernization Process
The following steps were taken to refactor the code:

1.  **Added Input Validation:** The `RegisterDto` was annotated with `jakarta.validation.constraints` (`@NotEmpty`, `@Email`) to enforce data integrity. The `@Valid` annotation was then added in the `AuthController` to trigger this validation.
2.  **Implemented Safe `Optional` Handling:** The `.get()` call was replaced with `.orElseThrow()`, which now throws a descriptive `ApiException` if the default role is not found in the database.
3.  **Ensured Transactional Atomicity:** The `@Transactional` annotation was added to the `register` method. This ensures that all database operations within the method are treated as a single, atomic unit.
4.  **Improved Code Conciseness:** The creation of the roles set was simplified from a multi-line `HashSet` instantiation to a single line using `Set.of()`.

## 4. Before/After Comparison

| Feature | Before | After | Benefit |
| :--- | :--- | :--- | :--- |
| **Input Validation** | None | `@Valid` on DTO | **Robustness & Security:** Prevents invalid data from entering the system. |
| **Error Handling** | Unsafe `.get()` on `Optional` | `.orElseThrow()` with custom exception | **Reliability:** Prevents unhandled exceptions and provides clear error messages. |
| **Data Integrity** | No transaction | `@Transactional` annotation | **Consistency:** Guarantees that user creation is an all-or-nothing operation. |
| **Code Readability** | Manual `HashSet` creation | `Set.of()` | **Conciseness:** Modern Java features make the code cleaner and easier to read. |

**Modernized Code:**
```java
@Override
@Transactional
public String register(RegisterDto registerDto) {
    // Check if email already exists
    if (userRepository.existsByEmail(registerDto.getEmail())) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "Email already exists.");
    }

    User user = new User();
    user.setFirstName(registerDto.getFirstName());
    user.setLastName(registerDto.getLastName());
    user.setEmail(registerDto.getEmail());
    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

    Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Default user role not found."));

    user.setRoles(Set.of(userRole));

    userRepository.save(user);

    return "User registered successfully!";
}
```

## Conclusion
The modernization process significantly improved the quality of the `register` method. It is now more secure, robust, maintainable, and aligned with modern Java and Spring Boot development standards. This task demonstrates how targeted refactoring, guided by an analysis of code smells and anti-patterns, can lead to substantial improvements in a legacy codebase. 