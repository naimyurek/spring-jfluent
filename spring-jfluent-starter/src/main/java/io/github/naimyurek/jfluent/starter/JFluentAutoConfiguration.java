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

import io.github.naimyurek.jfluent.AbstractValidator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class JFluentAutoConfiguration implements BeanDefinitionRegistryPostProcessor {

    private static String[] basePackages;

    public static void setBasePackages(String[] basePackages) {
        JFluentAutoConfiguration.basePackages = basePackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);
        scanner.addIncludeFilter(new AssignableTypeFilter(AbstractValidator.class));
        scanner.scan(basePackages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] validatorBeanNames = beanFactory.getBeanNamesForType(AbstractValidator.class);
        for (String beanName : validatorBeanNames) {
            AbstractValidator validator = (AbstractValidator) beanFactory.getBean(beanName);
            ResolvableType type = ResolvableType.forClass(validator.getClass()).as(AbstractValidator.class);
            Class<?> generic = type.getGeneric(0).resolve();
            if (generic != null) {
                SpringFluentValidatorAdapter adapter = new SpringFluentValidatorAdapter(validator, generic);
                beanFactory.registerSingleton(beanName + "Adapter", adapter);
            }
        }
    }
}
