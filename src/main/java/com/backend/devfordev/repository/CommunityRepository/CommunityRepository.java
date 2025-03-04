package com.backend.devfordev.repository.CommunityRepository;

import com.backend.devfordev.domain.CommunityEntity.Community;
import com.backend.devfordev.domain.enums.CommunityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findByCommunityCategory(CommunityCategory category);


    @Query("SELECT c, (SELECT COUNT(h) FROM Heart h WHERE h.likeId = c.id AND h.likeType = 'COMMUNITY') as likeCount " +
            "FROM Community c JOIN FETCH c.member " +
            "WHERE c.deletedAt IS NULL")
    List<Object[]> findAllWithLikesAndMember();



//    @Query("SELECT c.member, SUM(h.id) as totalLikes " +
//            "FROM Community c " +
//            "JOIN Heart h ON c.id = h.likeId " +
//            "WHERE h.likeType = 'COMMUNITY' " +  // 좋아요 타입이 'COMMUNITY'인 경우만 합산
//            "GROUP BY c.member " +               // 유저별로 그룹화
//            "ORDER BY totalLikes DESC " +        // 좋아요 수 기준으로 내림차순 정렬
//            "LIMIT 5")                           // 상위 5명만 가져오기
//    List<Object[]> findTop5UsersByTotalLikes();

    @Query("SELECT h.member, COUNT(h.id) as totalLikes " +
            "FROM Heart h " +
            "JOIN MemberInfo mi ON h.member.id = mi.member.id " +
            "WHERE h.likeType = 'COMMUNITY' " +
            "GROUP BY h.member.id, h.member.name, mi.nickname, mi.imageUrl " +
            "ORDER BY totalLikes DESC")
    List<Object[]> findTop5UsersByTotalLikes();




}
