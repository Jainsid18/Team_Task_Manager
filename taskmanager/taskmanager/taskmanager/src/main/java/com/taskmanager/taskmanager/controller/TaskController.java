package com.taskmanager.taskmanager.controller;

import com.taskmanager.taskmanager.entity.Task;
import com.taskmanager.taskmanager.entity.TaskStatus;
import com.taskmanager.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public Task createTask(@RequestParam String title,
                           @RequestParam String desc,
                           @RequestParam Long userId,
                           @RequestParam Long projectId,
                           @RequestParam String dueDate) {

        return taskService.createTask(
                title,
                desc,
                userId,
                projectId,
                LocalDate.parse(dueDate)
        );
    }


    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}/status")
    public Task updateStatus(@PathVariable Long id,
                             @RequestParam TaskStatus status) {

        return taskService.updateStatus(id, status);
    }
}
