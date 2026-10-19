package com.julianogn.tarefas.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.julianogn.tarefas.application.command.CreateTaskCommand;
import com.julianogn.tarefas.application.command.TaskFilter;
import com.julianogn.tarefas.application.dto.TaskView;
import com.julianogn.tarefas.application.sorting.TaskSortOrder;
import com.julianogn.tarefas.domain.event.DomainEvent;
import com.julianogn.tarefas.domain.event.TaskCreated;
import com.julianogn.tarefas.domain.exception.TaskNotFoundException;
import com.julianogn.tarefas.domain.model.Priority;
import com.julianogn.tarefas.domain.model.TaskStatus;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

    private final List<DomainEvent> published = new ArrayList<>();
    private final Clock clock = Clock.fixed(Instant.parse("2025-03-10T12:00:00Z"), ZoneOffset.UTC);
    private final TaskService service = new TaskService(new InMemoryTaskRepository(), published::addAll, clock);

    @Test
    void createPublishesEvent() {
        TaskView task = create("Ler Clean Code", Priority.MEDIUM, null);

        assertThat(task.status()).isEqualTo(TaskStatus.TODO);
        assertThat(published).singleElement().isInstanceOf(TaskCreated.class);
    }

    @Test
    void listFiltersByStatus() {
        TaskView first = create("A", Priority.LOW, null);
        create("B", Priority.LOW, null);
        service.complete(first.id());

        List<TaskView> done = service.list(new TaskFilter(TaskStatus.DONE, null, false, null));

        assertThat(done).extracting(TaskView::title).containsExactly("A");
    }

    @Test
    void listSortsByPriority() {
        create("Baixa", Priority.LOW, null);
        create("Alta", Priority.HIGH, null);

        List<TaskView> tasks = service.list(new TaskFilter(null, null, false, TaskSortOrder.PRIORITY));

        assertThat(tasks).extracting(TaskView::title).containsExactly("Alta", "Baixa");
    }

    @Test
    void listOnlyOverdue() {
        create("Atrasada", Priority.LOW, LocalDate.of(2025, 3, 1));
        create("No prazo", Priority.LOW, LocalDate.of(2025, 4, 1));

        List<TaskView> tasks = service.list(new TaskFilter(null, null, true, null));

        assertThat(tasks).extracting(TaskView::title).containsExactly("Atrasada");
    }

    @Test
    void unknownTaskIsNotFound() {
        assertThatThrownBy(() -> service.get(UUID.randomUUID().toString()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    private TaskView create(String title, Priority priority, LocalDate dueDate) {
        return service.create(new CreateTaskCommand(title, "", priority, dueDate));
    }
}
