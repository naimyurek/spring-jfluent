package io.github.naimyurek.jfluent;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationResultTest {

    @Test
    void isValid_shouldReturnTrue_whenErrorsListIsNull() {
        ValidationResult result = new ValidationResult(null);
        assertThat(result.isValid()).isTrue();
    }

    @Test
    void isValid_shouldReturnTrue_whenErrorsListIsEmpty() {
        ValidationResult result = new ValidationResult(Collections.emptyList());
        assertThat(result.isValid()).isTrue();
    }

    @Test
    void isValid_shouldReturnFalse_whenErrorsListIsNotEmpty() {
        ValidationResult result = new ValidationResult(List.of("Error 1"));
        assertThat(result.isValid()).isFalse();
    }

    @Test
    void getErrors_shouldReturnEmptyList_whenErrorsListIsNull() {
        ValidationResult result = new ValidationResult(null);
        assertThat(result.getErrors()).isEmpty();
    }

    @Test
    void getErrors_shouldReturnErrors_whenErrorsListIsNotEmpty() {
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        ValidationResult result = new ValidationResult(errors);
        assertThat(result.getErrors()).containsExactly("Error 1", "Error 2");
    }
}
