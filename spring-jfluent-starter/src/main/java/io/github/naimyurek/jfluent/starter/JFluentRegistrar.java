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

package io.github.naimyurek.jfluent.starter;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

public class JFluentRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        List<String> basePackages = AutoConfigurationPackages.get(registry);
        JFluentAutoConfiguration.setBasePackages(basePackages.toArray(new String[0]));

        if (!registry.containsBeanDefinition("jFluentAutoConfiguration")) {
            registry.registerBeanDefinition("jFluentAutoConfiguration", new org.springframework.beans.factory.support.GenericBeanDefinition(JFluentAutoConfiguration.class));
        }
    }
}
