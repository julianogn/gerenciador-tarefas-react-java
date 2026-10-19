package com.julianogn.tarefas.infrastructure;

import com.julianogn.tarefas.application.TaskService;
import com.julianogn.tarefas.domain.event.DomainEventPublisher;
import com.julianogn.tarefas.domain.repository.TaskRepository;
import java.time.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Composition root: wires framework-free application classes into Spring. */
@Configuration
public class BeanConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    TaskService taskService(TaskRepository repository, DomainEventPublisher publisher, Clock clock) {
        return new TaskService(repository, publisher, clock);
    }

    @Bean
    WebMvcConfigurer corsConfigurer(@Value("${app.cors.allowed-origins}") String[] allowedOrigins) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
