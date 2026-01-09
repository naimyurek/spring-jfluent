# spring-jfluent 🍃

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.naimyurek/spring-jfluent.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.naimyurek%22%20AND%20a:%22spring-jfluent%22)
[![Build Status](https://img.shields.io/github/workflow/status/naimyurek/spring-jfluent/Java%20CI)](https://github.com/naimyurek/spring-jfluent/actions)

> **A FluentValidation-inspired, type-safe, and annotation-free validation library for Spring Boot. Keep your POJOs clean and your logic fluent.** 🚀

---

## 💡 Why spring-jfluent?

In the standard Java ecosystem (Bean Validation / JSR-380), validation logic often clutters your domain models with annotations like `@NotNull`, `@Size`, or `@Pattern`. This mixes concerns and makes complex validation logic hard to read and test.

**spring-jfluent** solves this by separating validation rules from the model, using a fluent, type-safe syntax similar to .NET's popular FluentValidation library.

| Feature | ❌ Standard Bean Validation | ✅ spring-jfluent |
| :--- | :--- | :--- |
| **Logic Location** | Inside POJO / Entity | Separate Validator Classes |
| **Type Safety** | String-based errors possible | Fully Type-Safe (Lambdas) |
| **Complex Logic** | Hard (Custom Annotations required) | Easy (Inline Predicates) |
| **Readability** | Cluttered Models | Clean & Fluent |
| **Performance** | Heavy Reflection | Minimal / Lambda-based |

---

## 📦 Installation

Add the following dependency to your project.

### Maven
```xml
<dependency>
    <groupId>com.github.naimyurek</groupId>
    <artifactId>spring-jfluent</artifactId>
    <version>1.0.0</version>
</dependency>
````
🚀 Quick Start
1. Define your Model (POJO)
Notice there are no annotations here! Just a plain Java object.

````Java

public class Customer {
    private String name;
    private int age;
    private String email;

    // Getters and Setters...
}

````
2. Create a Validator
Extend AbstractValidator<T> and define your rules in the constructor using the fluent syntax. Mark it as a Spring @Component so spring-jfluent can auto-discover it.

````Java

import org.springframework.stereotype.Component;
import com.jfluent.core.AbstractValidator; // Example package

@Component
public class CustomerValidator extends AbstractValidator<Customer> {
    
    public CustomerValidator() {
        // Simple rules
        ruleFor(Customer::getName)
            .notNull("Name cannot be null")
            .must(name -> name.length() >= 3, "Name must be at least 3 characters long");

        // Numeric rules
        ruleFor(Customer::getAge)
            .must(age -> age >= 18, "Customer must be an adult (18+)");

        // Complex inline logic
        ruleFor(Customer::getEmail)
            .notNull("Email is required")
            .must(email -> email.contains("@") && email.endsWith(".com"), "Invalid email format");
    }
}
````
3. Use in Controller
Use the standard Spring @Valid annotation. That's it! spring-jfluent automatically hooks into Spring's validation mechanism.

````Java

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @PostMapping
    public ResponseEntity<String> createCustomer(@Valid @RequestBody Customer customer) {
        // If we reach here, the customer is valid!
        return ResponseEntity.ok("Customer is valid and created.");
    }
    
    // Standard Spring Exception Handling (MethodArgumentNotValidException) works as usual!
}
````
## 🛠 Features

* **Type-Safety:** Uses Java 8+ Lambdas (`Customer::getName`) to reference properties. If you rename a field, your compiler will let you know immediately.
* **Zero Boilerplate:** No need to manually register validators. The library auto-scans for `AbstractValidator` beans.
* **Spring Native:** Works seamlessly with `@Valid`, `@Validated`, and standard Spring `Errors` interface.
* **Extensible:** You can easily write your own custom rule extensions.

---

## 🤝 Contributing

Contributions are welcome! If you'd like to contribute:

1.  Fork the repository.
2.  Create a feature branch (`git checkout -b feature/amazing-feature`).
3.  Commit your changes.
4.  Open a Pull Request.

Please make sure to update tests as appropriate.

---

## 📄 License

Distributed under the **Apache License 2.0**. See `LICENSE` for more information.

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/naimyurek">Naim Yürek</a>
</p>

