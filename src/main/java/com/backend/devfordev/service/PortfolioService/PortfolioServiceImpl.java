package com.backend.devfordev.service.PortfolioService;

import com.backend.devfordev.apiPayload.code.status.ErrorStatus;

import com.backend.devfordev.apiPayload.exception.handler.*;

import com.backend.devfordev.converter.PortfolioConverter;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.domain.PortfolioEntity.*;
import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.dto.PortfolioDto.PortfolioRequest;
import com.backend.devfordev.dto.PortfolioDto.PortfolioResponse;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import com.backend.devfordev.repository.PortfolioRepository.*;
import com.backend.devfordev.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PortfolioServiceImpl implements PortfolioService{
    private final PortfolioRepository portfolioRepository;
    private final PortfolioLinkRepository portfolioLinkRepository;
    private final MemberRepository memberRepository;
    private final PortfolioEducationRepository portfolioEducationRepository;
    private final PortfolioAwardRepository portfolioAwardRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final PortfolioCareerRepository portfolioCareerRepository;
    private final S3Service s3Service;
    @Override
    @Transactional
    public PortfolioResponse.PortCreateResponse createPortfolio(PortfolioRequest.PortfolioCreateRequest request, Long userId, MultipartFile portImage) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.INVALID_MEMBER));

        String imageUrl;
        try {
            // 이미지 파일이 비어 있는지 확인
            if (portImage == null || portImage.isEmpty()) {
                // 기본 이미지 URL을 설정
                imageUrl = s3Service.saveDefaultProfileImage();
            } else {
                // 이미지 업로드 후 URL 반환
                imageUrl = s3Service.saveProfileImage(portImage);
            }
        } catch (IOException e) {
            throw new MemberHandler(ErrorStatus.IMAGE_UPLOAD_FAILED);
        }

//        // 이미지 URL 설정
//        request.setPortImageUrl(imageUrl);

        // 포트폴리오 생성
        Portfolio portfolio = PortfolioConverter.toPortfolio(request, member, imageUrl);
        portfolioRepository.save(portfolio);

        System.out.println(request.getAwards());
        System.out.println(request.getCareers());

        // 링크 리스트 순서 자동 설정 후 변환 및 저장
        List<PortfolioLink> links = PortfolioConverter.toPortfolioLinks(request.getLinks(), portfolio);
        for (int i = 0; i < links.size(); i++) {
            links.get(i).setOrderIndex(i + 1); // 자동 순서 설정
        }
        portfolioLinkRepository.saveAll(links);

        // 학력 리스트 순서 자동 설정 후 변환 및 저장
        List<PortfolioEducation> educations = PortfolioConverter.toEducationList(request.getEducations(), portfolio);
        for (int i = 0; i < educations.size(); i++) {
            educations.get(i).setOrderIndex(i + 1); // 자동 순서 설정
        }
        portfolioEducationRepository.saveAll(educations);

        // 수상 및 기타 리스트 순서 자동 설정 후 변환 및 저장
        List<PortfolioAward> awards = PortfolioConverter.toAwardList(request.getAwards(), portfolio);
        for (int i = 0; i < awards.size(); i++) {
            awards.get(i).setOrderIndex(i + 1); // 자동 순서 설정
        }
        portfolioAwardRepository.saveAll(awards);

        System.out.println(awards);

        // 경력 리스트 순서 자동 설정 후 변환 및 저장
        List<PortfolioCareer> careers = PortfolioConverter.toCareerList(request.getCareers(), portfolio);
        for (int i = 0; i < careers.size(); i++) {
            careers.get(i).setOrderIndex(i + 1); // 자동 순서 설정
        }
        portfolioCareerRepository.saveAll(careers);

        // 포트폴리오 응답 변환
        return PortfolioConverter.toPortfolioResponse(portfolio, links, educations, awards, careers);
    }

    @Override
    @Transactional
    public List<PortfolioResponse.PortfolioListResponse> getPortList(String position, Optional<String> searchTermOpt, String sortBy) {

        List<Object[]> results = portfolioRepository.findAllWithLikesAndMember();

        // 필터링 및 DTO 변환
        List<PortfolioResponse.PortfolioListResponse> portList = results.stream()
                .map(result -> {
                    Portfolio portfolio = (Portfolio) result[0];  // 첫 번째는 Community 객체
                    Long likeCount = (Long) result[1];            // 두 번째는 좋아요 수

                    // 포지션 필터링
                    if (position != null && !portfolio.getPortPosition().equalsIgnoreCase(position)) {
                        return null; // 포지션이 일치하지 않을 경우 제외
                    }
                    // 카테고리 필터링
                    // 검색 필터링: 제목, 작성자 이름, 내용 중 하나라도 일치하면 true 반환
                    if (searchTermOpt.isPresent()) {
                        String searchTerm = searchTermOpt.get().toLowerCase();
                        boolean matches = portfolio.getPortTitle().toLowerCase().contains(searchTerm) ||
                                portfolio.getMember().getName().toLowerCase().contains(searchTerm) ||
                                portfolio.getPortContent().toLowerCase().contains(searchTerm);
                        if (!matches) {
                            return null;
                        }
                    }
                    MemberInfo memberInfoEntity = memberInfoRepository.findByMember(portfolio.getMember());

                    // MemberInfo 생성
                    CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                            portfolio.getMember().getId(),
                            memberInfoEntity.getImageUrl(),
                            memberInfoEntity.getNickname()
                    );


                    // DTO 변환
                    return PortfolioConverter.toPorListResponse(portfolio, memberInfo, likeCount);
                })
                .filter(Objects::nonNull)  // 필터링에서 null이 반환된 경우 제거
                .collect(Collectors.toList());

        // 정렬 기준에 따른 정렬
        switch (sortBy.toLowerCase()) {
            case "likes":   // 좋아요 순 정렬
                portList.sort(Comparator.comparingLong(PortfolioResponse.PortfolioListResponse::getLikes).reversed());
                break;
            case "views":   // 조회수 순 정렬
                portList.sort(Comparator.comparingLong(PortfolioResponse.PortfolioListResponse::getViews).reversed());
                break;
            case "recent":  // 최신순 정렬 (기본)
            default:
                portList.sort(Comparator.comparing(PortfolioResponse.PortfolioListResponse::getCreatedAt).reversed());
                break;
        }

        return portList;
    }

    @Override
    @Transactional
    public void deletePortfolio(Long portId, Long userId) {
        Portfolio portfolio = portfolioRepository.findById(portId)
                .orElseThrow(() -> new PortfolioHandler(ErrorStatus.PORTFOLIO_NOT_FOUND));

        if (portfolio.getDeletedAt() != null) {
            throw new PortfolioHandler(ErrorStatus.PORTFOLIO_DELETED);
        }


        if (!portfolio.getMember().getId().equals(userId)) {
            throw new PortfolioHandler(ErrorStatus.UNAUTHORIZED_USER);
        }

        portfolio.deleteSoftly();

        portfolioRepository.save(portfolio);
    }
}
