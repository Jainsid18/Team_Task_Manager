package com.taskmanager.taskmanager.controller;

import com.taskmanager.taskmanager.entity.Task;
import com.taskmanager.taskmanager.entity.TaskStatus;
import com.taskmanager.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final TaskRepository taskRepository;

    @GetMapping
    public Map<String, Long> getDashboard() {

        List<Task> tasks = taskRepository.findAll();

        long total = tasks.size();

        long completed = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .count();

        long overdue = tasks.stream()
                .filter(t ->
                        t.getDueDate() != null &&
                                t.getDueDate().isBefore(LocalDate.now()) &&
                                t.getStatus() != TaskStatus.DONE
                )
                .count();

        return Map.of(
                "totalTasks", total,
                "completedTasks", completed,
                "overdueTasks", overdue
        );
    }
}
