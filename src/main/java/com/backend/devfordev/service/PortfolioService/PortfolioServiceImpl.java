package com.backend.devfordev.service.PortfolioService;

import com.backend.devfordev.apiPayload.code.status.ErrorStatus;

import com.backend.devfordev.apiPayload.exception.handler.*;

import com.backend.devfordev.converter.PortfolioConverter;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.domain.PortfolioEntity.*;
import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.dto.CustomPageResponse;
import com.backend.devfordev.dto.PortfolioDto.PortfolioRequest;
import com.backend.devfordev.dto.PortfolioDto.PortfolioResponse;
import com.backend.devfordev.repository.LikeRepository;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import com.backend.devfordev.repository.PortfolioRepository.*;
import com.backend.devfordev.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
    private final LikeRepository likeRepository;
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
    @Transactional(readOnly = true)
    public CustomPageResponse<PortfolioResponse.PortfolioListResponse> getPortList(
            String position,
            Optional<String> searchTermOpt,
            String sortBy,
            Pageable pageable
    ) {
        // ✅ 정렬을 Pageable에서 설정
        Sort sort;
        switch (sortBy.toLowerCase()) {
            case "likes" -> sort = Sort.by(Sort.Order.desc("likeCount"));
            case "views" -> sort = Sort.by(Sort.Order.desc("portViews"));
            default -> sort = Sort.by(Sort.Order.desc("createdAt"));  // 기본값: 최신순
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // ✅ 포트폴리오 조회 (Page<> 반환)
        Page<Object[]> results = portfolioRepository.findAllWithLikesAndMember(searchTermOpt, position, pageable);

        // ✅ 검색 및 필터링 적용 후 DTO 변환
        List<PortfolioResponse.PortfolioListResponse> filteredList = results.stream()
                .map(result -> {
                    Portfolio portfolio = (Portfolio) result[0];
                    Long likeCount = (Long) result[1];

                    // ✅ 포지션 필터링
                    if (position != null && !portfolio.getPortPosition().equalsIgnoreCase(position)) {
                        return null;
                    }

                    // ✅ 검색 필터링
                    if (searchTermOpt.isPresent()) {
                        String searchTerm = searchTermOpt.get().toLowerCase();
                        boolean matches = portfolio.getPortTitle().toLowerCase().contains(searchTerm) ||
                                portfolio.getMember().getName().toLowerCase().contains(searchTerm) ||
                                portfolio.getPortContent().toLowerCase().contains(searchTerm);
                        if (!matches) {
                            return null;
                        }
                    }

                    // ✅ MemberInfo 조회 및 변환
                    MemberInfo memberInfoEntity = memberInfoRepository.findByMember(portfolio.getMember());
                    CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                            portfolio.getMember().getId(),
                            memberInfoEntity.getImageUrl(),
                            memberInfoEntity.getNickname()
                    );

                    return PortfolioConverter.toPorListResponse(portfolio, memberInfo, likeCount);
                })
                .filter(Objects::nonNull)  // 필터링된 null 값 제거
                .collect(Collectors.toList());

        // ✅ 필터링된 결과를 다시 Page 객체로 변환
        Page<PortfolioResponse.PortfolioListResponse> pagedResponse =
                new PageImpl<>(filteredList, pageable, results.getTotalElements());

        return new CustomPageResponse<>(pagedResponse);
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

    @Override
    @Transactional(readOnly = true)
    public PortfolioResponse.PortDetailResponse getPortfolioDetail(Long portfolioId) {
        // ✅ 변경된 Repository 메서드 사용 (단일 객체 반환)
        Portfolio portfolio = portfolioRepository.findPortfolioById(portfolioId)
                .orElseThrow(() -> new PortfolioHandler(ErrorStatus.PORTFOLIO_NOT_FOUND));

        if (portfolio.getDeletedAt() != null) {
            throw new PortfolioHandler(ErrorStatus.PORTFOLIO_DELETED);
        }

        Long Likecount = likeRepository.countByPortId(portfolioId);

        // ✅ 추가 정보 조회 (링크, 학력, 수상, 경력)
        List<PortfolioLink> links = portfolioLinkRepository.findByPortfolio(portfolio);
        List<PortfolioEducation> educations = portfolioEducationRepository.findByPortfolio(portfolio);
        List<PortfolioAward> awards = portfolioAwardRepository.findByPortfolio(portfolio);
        List<PortfolioCareer> careers = portfolioCareerRepository.findByPortfolio(portfolio);

        // ✅ 컨버터를 활용하여 DTO 변환 후 반환
        return PortfolioConverter.toPortDetailResponse(portfolio, links, educations, awards, careers, Likecount);
    }


    @Transactional
    @Override
    public PortfolioResponse.PortCreateResponse updatePortfolio(Long portfolioId, PortfolioRequest.PortfolioCreateRequest request,  Long userId, MultipartFile portImage) {
        // ✅ 포트폴리오 존재 여부 확인
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포트폴리오가 존재하지 않습니다."));

        // ✅ 작성자인지 확인
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


        // ✅ 기존 Portfolio 데이터 수정
        PortfolioConverter.updatePortfolio(portfolio, request, imageUrl);

        // ✅ 기존 데이터 삭제 후 새롭게 저장 (링크, 학력, 수상, 경력)
        portfolioLinkRepository.deleteByPortfolio(portfolio);
        portfolioEducationRepository.deleteByPortfolio(portfolio);
        portfolioAwardRepository.deleteByPortfolio(portfolio);
        portfolioCareerRepository.deleteByPortfolio(portfolio);
        List<PortfolioAward> existingAwards = portfolioAwardRepository.findByPortfolio(portfolio);
        List<PortfolioLink> links = PortfolioConverter.updatePortfolioLinks(request.getLinks(), portfolio);
        List<PortfolioEducation> educations = PortfolioConverter.updateEducationList(request.getEducations(), portfolio);
        List<PortfolioAward> awards = PortfolioConverter.updateAwardList(request.getAwards(), existingAwards, portfolio);
        List<PortfolioCareer> careers = PortfolioConverter.updateCareerList(request.getCareers(), portfolio);

        portfolioLinkRepository.saveAll(links);
        portfolioEducationRepository.saveAll(educations);
        portfolioAwardRepository.saveAll(awards);
        portfolioCareerRepository.saveAll(careers);

        return PortfolioConverter.toPortfolioResponse(portfolio, links, educations, awards, careers);
    }
}

