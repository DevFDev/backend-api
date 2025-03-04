package com.backend.devfordev.repository.TeamRepository;

import com.backend.devfordev.domain.TeamEntity.TeamTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamTagRepository extends JpaRepository<TeamTag, Long> {
    Optional<TeamTag> findByName(String name);


    // 태그 이름 리스트로 태그 엔티티 조회
    List<TeamTag> findAllByNameIn(List<String> names);
}
