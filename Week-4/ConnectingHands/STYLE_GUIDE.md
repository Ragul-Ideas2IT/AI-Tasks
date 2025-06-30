# Code Style Guide

This project follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) for all Java code.

## Linting with Checkstyle

- The `Checkstyle.xml` configuration in this repository enforces Google Java Style.
- To check code style, run:

```sh
mvn checkstyle:check
```

- Fix any reported issues before committing code.

## Key Style Rules
- 2-space indentation
- 100-character line limit
- Javadoc for all public classes and methods
- Consistent naming conventions (camelCase for variables/methods, PascalCase for classes)
- No unused imports or variables

For more details, see the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). 