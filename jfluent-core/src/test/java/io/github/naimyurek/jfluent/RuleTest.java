package io.github.naimyurek.jfluent;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RuleTest {

    static class Person {
        String name;
        Integer age;

        Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    void validate_shouldReturnError_whenNotNullFails() {
        Rule<Person, String> rule = new Rule<>(p -> p.name);
        rule.notNull();

        Person person = new Person(null, 25);
        List<String> errors = rule.validate(person);

        assertThat(errors).containsExactly("must not be null.");
    }

    @Test
    void validate_shouldPass_whenNotNullSucceeds() {
        Rule<Person, String> rule = new Rule<>(p -> p.name);
        rule.notNull();

        Person person = new Person("John", 25);
        List<String> errors = rule.validate(person);

        assertThat(errors).isEmpty();
    }

    @Test
    void validate_shouldReturnError_whenMustFails() {
        Rule<Person, Integer> rule = new Rule<>(p -> p.age);
        rule.must(age -> age >= 18, "must be at least 18.");

        Person person = new Person("John", 17);
        List<String> errors = rule.validate(person);

        assertThat(errors).containsExactly("must be at least 18.");
    }

    @Test
    void validate_shouldPass_whenMustSucceeds() {
        Rule<Person, Integer> rule = new Rule<>(p -> p.age);
        rule.must(age -> age >= 18, "must be at least 18.");

        Person person = new Person("John", 18);
        List<String> errors = rule.validate(person);

        assertThat(errors).isEmpty();
    }

    @Test
    void validate_shouldCheckMultiplePredicates() {
        Rule<Person, Integer> rule = new Rule<>(p -> p.age);
        rule.notNull()
            .must(age -> age != null && age >= 18, "must be at least 18.");

        Person person = new Person("John", null);
        List<String> errors = rule.validate(person);

        assertThat(errors).contains("must not be null.");
        // Depending on implementation, it might also contain the second error.
        // With the fix (age != null check), the second predicate returns false, so it ADDS the second error.
        assertThat(errors).contains("must be at least 18.");
    }

    @Test
    void validate_shouldReturnMultipleErrors() {
        Rule<Person, Integer> rule = new Rule<>(p -> p.age);
        rule.must(age -> age > 0, "positive")
            .must(age -> age % 2 == 0, "even");

        Person person = new Person("John", -1);
        List<String> errors = rule.validate(person);

        assertThat(errors).containsExactly("positive", "even");
    }
}
