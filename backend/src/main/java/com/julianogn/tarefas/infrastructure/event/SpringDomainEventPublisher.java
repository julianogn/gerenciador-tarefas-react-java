package com.julianogn.tarefas.infrastructure.event;

import com.julianogn.tarefas.domain.event.DomainEvent;
import com.julianogn.tarefas.domain.event.DomainEventPublisher;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    SpringDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(List<DomainEvent> events) {
        events.forEach(publisher::publishEvent);
    }
}
