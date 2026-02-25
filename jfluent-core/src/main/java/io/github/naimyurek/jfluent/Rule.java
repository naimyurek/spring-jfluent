/*
 * Copyright 2024-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.naimyurek.jfluent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Rule<T, K> {

    private final Function<T, K> propertyGetter;
    private final List<Predicate<K>> predicates = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();

    public Rule(Function<T, K> propertyGetter) {
        this.propertyGetter = propertyGetter;
    }

    public Rule<T, K> withMessage(String message) {
        if (!errorMessages.isEmpty()) {
            errorMessages.set(errorMessages.size() - 1, message);
        }
        return this;
    }

    public Rule<T, K> notNull() {
        predicates.add(p -> p != null);
        errorMessages.add("must not be null.");
        return this;
    }

    public Rule<T, K> notEmpty() {
        return must(value -> {
            if (value == null) return false;
            if (value instanceof CharSequence) return ((CharSequence) value).length() > 0;
            if (value instanceof Collection) return !((Collection<?>) value).isEmpty();
            if (value instanceof Map) return !((Map<?, ?>) value).isEmpty();
            if (value instanceof Object[]) return ((Object[]) value).length > 0;
            return true;
        }, "must not be empty.");
    }

    public Rule<T, K> length(int min, int max) {
        return must(value -> {
            if (value == null) return true;
            if (value instanceof CharSequence) {
                int length = ((CharSequence) value).length();
                return length >= min && length <= max;
            }
            return false;
        }, "must be between " + min + " and " + max + " characters.");
    }

    @SuppressWarnings("unchecked")
    public Rule<T, K> greaterThan(K min) {
        return must(value -> {
            if (value == null) return true;
            if (min instanceof Comparable) {
                return ((Comparable<K>) min).compareTo(value) < 0;
            }
            return false;
        }, "must be greater than " + min + ".");
    }

    @SuppressWarnings("unchecked")
    public Rule<T, K> lessThan(K max) {
        return must(value -> {
            if (value == null) return true;
            if (max instanceof Comparable) {
                return ((Comparable<K>) max).compareTo(value) > 0;
            }
            return false;
        }, "must be less than " + max + ".");
    }

    @SuppressWarnings("unchecked")
    public Rule<T, K> greaterThanOrEqualTo(K min) {
        return must(value -> {
            if (value == null) return true;
            if (min instanceof Comparable) {
                return ((Comparable<K>) min).compareTo(value) <= 0;
            }
            return false;
        }, "must be greater than or equal to " + min + ".");
    }

    @SuppressWarnings("unchecked")
    public Rule<T, K> lessThanOrEqualTo(K max) {
        return must(value -> {
            if (value == null) return true;
            if (max instanceof Comparable) {
                return ((Comparable<K>) max).compareTo(value) >= 0;
            }
            return false;
        }, "must be less than or equal to " + max + ".");
    }

    public Rule<T, K> matches(String regex) {
        return must(value -> {
            if (value == null) return true;
            if (value instanceof CharSequence) {
                return Pattern.compile(regex).matcher((CharSequence) value).matches();
            }
            return false;
        }, "must match pattern " + regex + ".");
    }

    public Rule<T, K> emailAddress() {
        return matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").withMessage("must be a valid email address.");
    }

    public Rule<T, K> must(Predicate<K> predicate, String errorMessage) {
        predicates.add(predicate);
        errorMessages.add(errorMessage);
        return this;
    }

    public List<String> validate(T entity) {
        K propertyValue = propertyGetter.apply(entity);
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < predicates.size(); i++) {
            if (!predicates.get(i).test(propertyValue)) {
                errors.add(errorMessages.get(i));
            }
        }
        return errors;
    }
}
