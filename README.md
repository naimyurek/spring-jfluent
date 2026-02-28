# Spring JFluent

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Java Version](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-green)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

**Spring JFluent** is a lightweight, fluent validation library for Java Spring Boot applications, inspired by .NET's [FluentValidation](https://fluentvalidation.net/). It allows you to define validation rules in a clean, readable, and separate way from your domain models.

## Features

- **Fluent API:** Chainable validation rules for clean and readable code.
- **Separation of Concerns:** Define validation logic in dedicated validator classes, keeping your models clean.
- **Spring Integration:** Seamless integration with Spring Boot's dependency injection and validation framework.
- **Extensible:** Easily add custom validation logic using lambda expressions.

## Prerequisites

- Java 17 or higher
- Spring Boot 3.2.2 or higher
- Maven 3.6+

## Installation

Add the `spring-jfluent-starter` dependency to your `pom.xml`.

```xml
<dependency>
    <groupId>io.github.naimyurek</groupId>
    <artifactId>spring-jfluent-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

> **Note:** Since this is a snapshot version, ensure you have built the project locally using `mvn install` or configured a snapshot repository if hosted remotely.

## Quick Start

Follow these steps to integrate Spring JFluent into your application.

### 1. Enable JFluent Validation

Add the `@EnableJFluentValidation` annotation to your main application class or a configuration class.

```java
import io.github.naimyurek.jfluent.starter.EnableJFluentValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJFluentValidation
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 2. Define a Domain Model

Create a simple POJO class representing the data you want to validate.

```java
public class Customer {
    private String name;
    private String email;
    private int age;

    // Getters and Setters
}
```

### 3. Create a Validator

Extend `AbstractValidator<T>` and define your rules in the constructor using the `ruleFor` method.

```java
import io.github.naimyurek.jfluent.AbstractValidator;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidator extends AbstractValidator<Customer> {
    public CustomerValidator() {
        // Name validation
        ruleFor(Customer::getName)
            .notNull()
            .notEmpty()
            .length(2, 50)
            .withMessage("Name must be between 2 and 50 characters.");

        // Email validation
        ruleFor(Customer::getEmail)
            .notNull()
            .emailAddress();

        // Age validation
        ruleFor(Customer::getAge)
            .greaterThanOrEqualTo(18)
            .withMessage("Customer must be at least 18 years old.");
    }
}
```

### 4. Use in a Controller

Inject the validator into your controller and register it using `@InitBinder`. Use the `@Valid` annotation to trigger validation.

**Note:** The library automatically creates a Spring `Validator` adapter bean for your validator. The bean name is your validator's bean name suffixed with `Adapter` (e.g., `customerValidatorAdapter`).

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    @Qualifier("customerValidatorAdapter")
    private Validator validator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(@Valid @RequestBody Customer customer) {
        return ResponseEntity.ok("Customer is valid!");
    }
}
```

## Supported Validation Rules

The library provides a set of built-in validation rules:

| Rule | Description |
|------|-------------|
| `notNull()` | Ensures the property is not null. |
| `notEmpty()` | Ensures strings, collections, maps, or arrays are not empty. |
| `length(min, max)` | Validates that a string's length is within the specified range. |
| `emailAddress()` | Validates that a string is a valid email address format. |
| `matches(regex)` | Validates that a string matches the given regular expression. |
| `greaterThan(min)` | Validates that a comparable value is strictly greater than `min`. |
| `greaterThanOrEqualTo(min)` | Validates that a comparable value is greater than or equal to `min`. |
| `lessThan(max)` | Validates that a comparable value is strictly less than `max`. |
| `lessThanOrEqualTo(max)` | Validates that a comparable value is less than or equal to `max`. |
| `must(predicate, message)` | Allows defining custom validation logic using a lambda expression. |

### Custom Validation Example

You can implement complex validation logic using the `must` rule:

```java
ruleFor(Customer::getName)
    .must(name -> !name.contains("admin"), "Name cannot contain 'admin'.");
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
