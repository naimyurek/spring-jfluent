package io.github.naimyurek.jfluent;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractValidatorTest {

    static class User {
        String username;
        String email;

        User(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }

    static class UserValidator extends AbstractValidator<User> {
        UserValidator() {
            ruleFor(user -> user.username)
                .notNull()
                .must(name -> name.length() >= 3, "Username too short");

            ruleFor(user -> user.email)
                .notNull()
                .must(email -> email.contains("@"), "Invalid email");
        }
    }

    @Test
    void validate_shouldReturnAllErrors() {
        UserValidator validator = new UserValidator();
        User user = new User("Jo", "invalid-email");

        ValidationResult result = validator.validate(user);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).containsExactlyInAnyOrder(
            "Username too short",
            "Invalid email"
        );
    }

    @Test
    void validate_shouldPass_whenAllRulesSatisfied() {
        UserValidator validator = new UserValidator();
        User user = new User("John", "john@example.com");

        ValidationResult result = validator.validate(user);

        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
    }
}
