package com.julianogn.tarefas.application;

import com.julianogn.tarefas.application.command.CreateTaskCommand;
import com.julianogn.tarefas.application.command.TaskFilter;
import com.julianogn.tarefas.application.command.UpdateTaskCommand;
import com.julianogn.tarefas.application.dto.TaskView;
import com.julianogn.tarefas.domain.event.DomainEventPublisher;
import com.julianogn.tarefas.domain.exception.TaskNotFoundException;
import com.julianogn.tarefas.domain.model.Task;
import com.julianogn.tarefas.domain.model.TaskId;
import com.julianogn.tarefas.domain.model.Title;
import com.julianogn.tarefas.domain.repository.TaskRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Application service: coordinates use cases. Business rules live in the domain,
 * and this class has no framework dependency.
 */
public class TaskService {

    private final TaskRepository repository;
    private final DomainEventPublisher eventPublisher;
    private final Clock clock;

    public TaskService(TaskRepository repository, DomainEventPublisher eventPublisher, Clock clock) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    public TaskView create(CreateTaskCommand command) {
        Task task = Task.create(
                new Title(command.title()), command.description(), command.priority(), command.dueDate(), clock);
        return persist(task);
    }

    public TaskView update(String id, UpdateTaskCommand command) {
        Task task = load(id);
        task.edit(new Title(command.title()), command.description(), command.priority(), command.dueDate());
        return persist(task);
    }

    public TaskView get(String id) {
        return toView(load(id));
    }

    public List<TaskView> list(TaskFilter filter) {
        return repository.findAll().stream()
                .filter(filter.toPredicate(today()))
                .sorted(filter.sortOrder().comparator())
                .map(this::toView)
                .toList();
    }

    public TaskView start(String id) {
        return transition(id, Task::start);
    }

    public TaskView complete(String id) {
        return transition(id, Task::complete);
    }

    public TaskView reopen(String id) {
        return transition(id, Task::reopen);
    }

    public void delete(String id) {
        repository.delete(load(id).getId());
    }

    private TaskView transition(String id, BiConsumer<Task, Clock> action) {
        Task task = load(id);
        action.accept(task, clock);
        return persist(task);
    }

    private Task load(String id) {
        TaskId taskId = TaskId.of(id);
        return repository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    private TaskView persist(Task task) {
        repository.save(task);
        eventPublisher.publish(task.pullEvents());
        return toView(task);
    }

    private TaskView toView(Task task) {
        return TaskView.from(task, today());
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }
}
