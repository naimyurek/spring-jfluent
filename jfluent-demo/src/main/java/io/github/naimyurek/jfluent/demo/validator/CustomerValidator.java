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

package io.github.naimyurek.jfluent.demo.validator;

import io.github.naimyurek.jfluent.AbstractValidator;
import io.github.naimyurek.jfluent.demo.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidator extends AbstractValidator<Customer> {
    public CustomerValidator() {
        ruleFor(Customer::getName).notNull().must(name -> !name.isEmpty(), "Name must not be empty.");
        ruleFor(Customer::getEmail).notNull().must(email -> email.contains("@"), "Email must be valid.");
        ruleFor(Customer::getAge).must(age -> age >= 18, "Must be 18 or older.");
    }
}
