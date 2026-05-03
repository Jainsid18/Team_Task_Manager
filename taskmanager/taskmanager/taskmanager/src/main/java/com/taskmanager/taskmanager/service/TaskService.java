package com.taskmanager.taskmanager.service;

import com.taskmanager.taskmanager.entity.Project;
import com.taskmanager.taskmanager.entity.Task;
import com.taskmanager.taskmanager.entity.TaskStatus;
import com.taskmanager.taskmanager.entity.User;
import com.taskmanager.taskmanager.repository.ProjectRepository;
import com.taskmanager.taskmanager.repository.TaskRepository;
import com.taskmanager.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public Task createTask(String title, String desc,
                           Long userId, Long projectId,
                           LocalDate dueDate) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = Task.builder()
                .title(title)
                .description(desc)
                .status(TaskStatus.TODO)
                .assignedTo(user)
                .project(project)
                .dueDate(dueDate)
                .build();

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateStatus(Long taskId, TaskStatus status) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);
        return taskRepository.save(task);
    }
}
