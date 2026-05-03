package com.taskmanager.taskmanager.controller;

import com.taskmanager.taskmanager.entity.Project;
import com.taskmanager.taskmanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public Project createProject(@RequestParam String name,
                                 @RequestParam Long userId) {
        return projectService.createProjects(name, userId);
    }


    @PostMapping("/{projectId}/add-member")
    public String addMember(@PathVariable Long projectId,
                            @RequestParam Long userId) {

        projectService.addMember(projectId, userId);
        return "Member added successfully";
    }
}
