package com.backend.devfordev.domain.ProjectEntity;

import com.backend.devfordev.domain.ProjectEntity.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_link")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String url;
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_id")
    private Project project;
}
