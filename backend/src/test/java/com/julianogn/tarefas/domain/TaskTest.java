package com.julianogn.tarefas.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.julianogn.tarefas.domain.event.TaskCreated;
import com.julianogn.tarefas.domain.event.TaskStatusChanged;
import com.julianogn.tarefas.domain.exception.DomainException;
import com.julianogn.tarefas.domain.exception.InvalidStatusTransitionException;
import com.julianogn.tarefas.domain.model.Priority;
import com.julianogn.tarefas.domain.model.Task;
import com.julianogn.tarefas.domain.model.TaskStatus;
import com.julianogn.tarefas.domain.model.Title;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class TaskTest {

    private final Clock clock = Clock.fixed(Instant.parse("2025-03-10T12:00:00Z"), ZoneOffset.UTC);

    @Test
    void newTaskStartsAsTodoAndRecordsCreation() {
        Task task = newTask(null);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(task.pullEvents()).singleElement().isInstanceOf(TaskCreated.class);
        assertThat(task.pullEvents()).isEmpty();
    }

    @Test
    void completingRecordsStatusChange() {
        Task task = newTask(null);
        task.pullEvents();

        task.complete(clock);

        assertThat(task.pullEvents()).singleElement()
                .isEqualTo(new TaskStatusChanged(task.getId(), TaskStatus.TODO, TaskStatus.DONE, clock.instant()));
    }

    @Test
    void doneTaskCannotBeStarted() {
        Task task = newTask(null);
        task.complete(clock);

        assertThatThrownBy(() -> task.start(clock)).isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void overdueOnlyWhenPastDueAndNotDone() {
        Task task = newTask(LocalDate.of(2025, 3, 1));
        LocalDate today = LocalDate.of(2025, 3, 10);

        assertThat(task.isOverdue(today)).isTrue();
        task.complete(clock);
        assertThat(task.isOverdue(today)).isFalse();
    }

    @Test
    void titleMustNotBeBlank() {
        assertThatThrownBy(() -> new Title("   ")).isInstanceOf(DomainException.class);
    }

    @Test
    void titleIsTrimmed() {
        assertThat(new Title("  Estudar DDD ").value()).isEqualTo("Estudar DDD");
    }

    private Task newTask(LocalDate dueDate) {
        return Task.create(new Title("Revisar PR"), "", Priority.HIGH, dueDate, clock);
    }
}
