package com.matchcraft.controller;

import com.matchcraft.domain.entity.Project;
import com.matchcraft.dto.ProjectDto;
import com.matchcraft.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectDto> dtos = projects.stream().map(p -> {
            ProjectDto pDto = new ProjectDto();
            pDto.setId(p.getId());
            pDto.setGithubId(p.getGithubId());
            pDto.setNom(p.getNom());
            pDto.setDescription(p.getDescription());
            pDto.setUrl(p.getUrl());
            return pDto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
