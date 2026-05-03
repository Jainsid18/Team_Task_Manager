package com.taskmanager.taskmanager.service;

import com.taskmanager.taskmanager.entity.*;
import com.taskmanager.taskmanager.repository.ProjectMemberRepository;
import com.taskmanager.taskmanager.repository.ProjectRepository;
import com.taskmanager.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository memberRepository;

    public Project createProjects(String name,Long userId ){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Project project=Project.builder().name(name).createdBy(user).build();

        return  projectRepository.save(project);
    }

    public void addMember(Long projectId,Long userId){
        Project project= projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(user)
                .build();

        memberRepository.save(member);
    }
}
