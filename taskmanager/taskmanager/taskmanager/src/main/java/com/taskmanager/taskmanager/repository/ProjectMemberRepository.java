package com.taskmanager.taskmanager.repository;

import com.taskmanager.taskmanager.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
}
