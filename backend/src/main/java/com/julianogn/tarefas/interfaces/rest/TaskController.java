package com.julianogn.tarefas.interfaces.rest;

import com.julianogn.tarefas.application.TaskService;
import com.julianogn.tarefas.application.command.TaskFilter;
import com.julianogn.tarefas.application.dto.TaskView;
import com.julianogn.tarefas.application.sorting.TaskSortOrder;
import com.julianogn.tarefas.domain.model.Priority;
import com.julianogn.tarefas.domain.model.TaskStatus;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Controller (MVC): maps HTTP to use cases; the JSON view is the TaskView record. */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskView> list(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(defaultValue = "false") boolean overdue,
            @RequestParam(defaultValue = "CREATION") TaskSortOrder sort) {
        return service.list(new TaskFilter(status, priority, overdue, sort));
    }

    @GetMapping("/{id}")
    public TaskView get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<TaskView> create(@Valid @RequestBody TaskRequest request) {
        TaskView created = service.create(request.toCreateCommand());
        return ResponseEntity.created(URI.create("/api/tasks/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public TaskView update(@PathVariable String id, @Valid @RequestBody TaskRequest request) {
        return service.update(id, request.toUpdateCommand());
    }

    @PostMapping("/{id}/start")
    public TaskView start(@PathVariable String id) {
        return service.start(id);
    }

    @PostMapping("/{id}/complete")
    public TaskView complete(@PathVariable String id) {
        return service.complete(id);
    }

    @PostMapping("/{id}/reopen")
    public TaskView reopen(@PathVariable String id) {
        return service.reopen(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
