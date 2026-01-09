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
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Rule<T, K> {

    private final Function<T, K> propertyGetter;
    private final List<Predicate<K>> predicates = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();

    public Rule(Function<T, K> propertyGetter) {
        this.propertyGetter = propertyGetter;
    }

    public Rule<T, K> notNull() {
        predicates.add(p -> p != null);
        errorMessages.add("must not be null.");
        return this;
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
