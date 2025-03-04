package com.backend.devfordev.converter;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.PortfolioEntity.*;
import com.backend.devfordev.domain.enums.AwardType;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.dto.PortfolioDto.PortfolioRequest;
import com.backend.devfordev.dto.PortfolioDto.PortfolioResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PortfolioConverter {

    public static Portfolio toPortfolio(PortfolioRequest.PortfolioCreateRequest request, Member member, String imageUrl) {

        Portfolio portfolio = Portfolio.builder()
                .portTitle(request.getPortTitle())
                .portContent(request.getPortContent())
                .portPosition(request.getPortPosition())
                .portImageUrl(imageUrl)
                .member(member)
                .portViews(0L)
                .build();

        // techStacks 리스트를 쉼표로 구분된 문자열로 변환하여 저장
        portfolio.setTechStacks(Collections.singletonList(String.join(",", request.getTechStacks())));
        portfolio.setTags(Collections.singletonList(String.join(",", request.getTags())));
        return portfolio;
    }


    public static List<PortfolioLink> toPortfolioLinks(List<PortfolioRequest.PortfolioCreateRequest.LinkRequest> linkRequests, Portfolio portfolio) {
        return linkRequests.stream()
                .map(linkRequest -> PortfolioLink.builder()
                        .type(linkRequest.getType())
                        .url(linkRequest.getUrl())
                        .portfolio(portfolio)
                        .build())
                .collect(Collectors.toList());
    }

    public static List<PortfolioEducation> toEducationList(List<PortfolioRequest.PortfolioCreateRequest.EducationRequest> educationRequests, Portfolio portfolio) {
        return educationRequests.stream()
                .map(educationRequest -> PortfolioEducation.builder()
                        .level(educationRequest.getLevel())
                        .institutionName(educationRequest.getInstitutionName())
                        .major(educationRequest.getMajor())
                        .admissionDate(educationRequest.getAdmissionDate())
                        .graduationDate(educationRequest.getGraduationDate())
                        .graduationStatus(educationRequest.getGraduationStatus())
                        .isTransfer(Boolean.valueOf(educationRequest.getIsTransfer()))
                        .grade(educationRequest.getGrade())
                        .gradeScale(educationRequest.getGradeScale())
                        .orderIndex(educationRequests.indexOf(educationRequest) + 1) // 자동 순서 설정
                        .portfolio(portfolio)
                        .build())
                .collect(Collectors.toList());
    }

    public static List<PortfolioAward> toAwardList(List<PortfolioRequest.PortfolioCreateRequest.AwardRequest> awardRequests, Portfolio portfolio) {
        // 인덱스를 추적하며 처리
        AtomicInteger index = new AtomicInteger(1);

        return awardRequests.stream().map(awardRequest -> {
            int orderIndex = index.getAndIncrement(); // 인덱스 값을 추적

            if (awardRequest instanceof PortfolioRequest.PortfolioCreateRequest.AwardRequest.CompetitionAwardRequest req) {
                return new CompetitionAward(
                        null,
                        orderIndex,
                        req.getAwardType(),
                        portfolio,
                        req.getCompetitionName(),
                        req.getHostingInstitution(),
                        req.getCompetitionDate()
                );
            } else if (awardRequest instanceof PortfolioRequest.PortfolioCreateRequest.AwardRequest.CertificationAwardRequest req) {
                return new CertificationAward(
                        null,
                        orderIndex,
                        req.getAwardType(),
                        portfolio,
                        req.getCertificateName(),
                        req.getIssuer(),
                        req.getPassingDate()
                );
            } else if (awardRequest instanceof PortfolioRequest.PortfolioCreateRequest.AwardRequest.LanguageAwardRequest req) {
                return new LanguageAward(
                        null,
                        orderIndex,
                        req.getAwardType(),
                        portfolio,
                        req.getLanguage(),
                        req.getTestName(),
                        req.getScore(),
                        req.getObtainedDate()
                );
            } else if (awardRequest instanceof PortfolioRequest.PortfolioCreateRequest.AwardRequest.ActivityAwardRequest req) {
                return new ActivityAward(
                        null,
                        orderIndex,
                        req.getAwardType(),
                        portfolio,
                        req.getActivityName(),
                        req.getStartDate(),
                        req.getEndDate()
                );
            }
            throw new IllegalArgumentException("Invalid award type: " + awardRequest.getAwardType());
        }).collect(Collectors.toList());
    }


//    public static List<PortfolioAward> toAwardList(List<PortfolioRequest.PortfolioCreateRequest.AwardRequest> awardRequests, Portfolio portfolio) {
//        return awardRequests.stream().map(awardRequest -> {
//            switch (awardRequest.getAwardType()) {
//                case "COMPETITION":
//                    return new CompetitionAward(
//                            null,
//                            awardRequests.indexOf(awardRequest) + 1,
//                            awardRequest.getAwardType(),
//                            portfolio,
//                            awardRequest.getCompetitionName(),
//                            awardRequest.getHostingInstitution(),
//                            awardRequest.getCompetitionDate()
//                    );
//                case "CERTIFICATE":
//                    return new CertificationAward(
//                            null,
//                            awardRequests.indexOf(awardRequest) + 1,
//                            awardRequest.getAwardType(),
//                            portfolio,
//                            awardRequest.getCertificateName(),
//                            awardRequest.getIssuer(),
//                            awardRequest.getPassingYear()
//                    );
//                case "LANGUAGE":
//                    return new LanguageAward(
//                            null,
//                            awardRequests.indexOf(awardRequest) + 1,
//                            awardRequest.getAwardType(),
//                            portfolio,
//                            awardRequest.getLanguage(),
//                            awardRequest.getLevel(),
//                            awardRequest.getTestName(),
//                            awardRequest.getScore(),
//                            awardRequest.getObtainedDate()
//                    );
//                case "ACTIVITY":
//                    return new ActivityAward(
//                            null,
//                            awardRequests.indexOf(awardRequest) + 1,
//                            awardRequest.getAwardType(),
//                            portfolio,
//                            awardRequest.getActivityName(),
//                            awardRequest.getStartDate(),
//                            awardRequest.getEndDate(),
//                            awardRequest.getDescription()
//                    );
//                default:
//                    throw new IllegalArgumentException("Invalid award type: " + awardRequest.getAwardType());
//            }
//        }).collect(Collectors.toList());
//    }

    public static List<PortfolioCareer> toCareerList(List<PortfolioRequest.PortfolioCreateRequest.CareerRequest> careerRequests, Portfolio portfolio) {
        return careerRequests.stream()
                .map(careerRequest -> PortfolioCareer.builder()
                        .companyName(careerRequest.getCompanyName())
                        .position(careerRequest.getPosition())
                        .startDate(careerRequest.getStartDate())
                        .endDate(careerRequest.getEndDate())
                        .isCurrent(careerRequest.getIsCurrent())
                        .level(careerRequest.getLevel())
                        .description(careerRequest.getDescription())
                        .portfolio(portfolio)
                        .build())
                .collect(Collectors.toList());
    }



    public static PortfolioResponse.PortCreateResponse toPortfolioResponse(
            Portfolio portfolio,
            List<PortfolioLink> links,
            List<PortfolioEducation> educations,
            List<PortfolioAward> awards,
            List<PortfolioCareer> careers
    ) {
        List<PortfolioResponse.PortCreateResponse.LinkResponse> linkResponses = links.stream()
                .map(link -> new PortfolioResponse.PortCreateResponse.LinkResponse(
                        link.getType(),
                        link.getUrl(),
                        link.getOrderIndex()
                ))
                .collect(Collectors.toList());

        List<PortfolioResponse.PortCreateResponse.EducationResponse> educationResponses = educations.stream()
                .map(education -> new PortfolioResponse.PortCreateResponse.EducationResponse(
                        education.getId(),
                        education.getLevel(),
                        education.getInstitutionName(),
                        education.getMajor(),
                        education.getAdmissionDate(),
                        education.getGraduationDate(),
                        education.getGraduationStatus(),
                        education.getIsTransfer(),
                        education.getGrade(),
                        education.getGradeScale(),
                        education.getOrderIndex()
                ))
                .collect(Collectors.toList());

        // Award Responses - 각 Award Type별로 구체적인 Response 생성
        List<PortfolioResponse.PortCreateResponse.AwardResponse> awardResponses = awards.stream()
                .map(award -> {

                    if (award instanceof CompetitionAward) {
                        CompetitionAward compAward = (CompetitionAward) award;
                        return new PortfolioResponse.PortCreateResponse.AwardResponse.CompetitionAwardResponse(
                                compAward.getId(),
                                AwardType.COMPETITION,
                                compAward.getOrderIndex(),
                                compAward.getCompetitionName(),
                                compAward.getHostingInstitution(),
                                compAward.getCompetitionDate()
                        );
                    } else if (award instanceof CertificationAward) {
                        CertificationAward certAward = (CertificationAward) award;
                        return new PortfolioResponse.PortCreateResponse.AwardResponse.CertificateAwardResponse(
                                certAward.getId(),
                                AwardType.CERTIFICATE,
                                certAward.getOrderIndex(),
                                certAward.getCertificationName(),
                                certAward.getIssuingInstitution(),
                                certAward.getPassingDate()
                        );
                    } else if (award instanceof LanguageAward) {
                        LanguageAward langAward = (LanguageAward) award;
                        return new PortfolioResponse.PortCreateResponse.AwardResponse.LanguageAwardResponse(
                                langAward.getId(),
                                AwardType.LANGUAGE,
                                langAward.getOrderIndex(),
                                langAward.getLanguage(),
                                langAward.getTestName(),
                                langAward.getScore(),
                                langAward.getObtainedDate()
                        );
                    } else if (award instanceof ActivityAward) {
                        ActivityAward actAward = (ActivityAward) award;
                        return new PortfolioResponse.PortCreateResponse.AwardResponse.ActivityAwardResponse(
                                actAward.getId(),
                                AwardType.ACTIVITY,
                                actAward.getOrderIndex(),
                                actAward.getActivityName(),
                                actAward.getStartDate(),
                                actAward.getEndDate()
                        );
                    } else {
                        throw new IllegalArgumentException("Invalid award type: " + award.getAwardType());
                    }
                })
                .collect(Collectors.toList());

        List<PortfolioResponse.PortCreateResponse.CareerResponse> careerResponses = careers.stream()
                .map(career -> new PortfolioResponse.PortCreateResponse.CareerResponse(
                        career.getId(),
                        career.getCompanyName(),
                        career.getOrderIndex(),
                        career.getPosition(),
                        career.getStartDate(),
                        career.getEndDate(),
                        career.getIsCurrent(),
                        career.getDescription()
                ))
                .collect(Collectors.toList());

        // techStacks 문자열을 리스트로 변환하여 설정
        List<String> techStacks = portfolio.getTechStacks() != null ? portfolio.getTechStacks() : new ArrayList<>();
        List<String> tags = portfolio.getTags() != null ? portfolio.getTags() : new ArrayList<>();

        return new PortfolioResponse.PortCreateResponse(
                portfolio.getId(),
                portfolio.getMember().getId(),
                portfolio.getPortTitle(),
                portfolio.getPortContent(),
                portfolio.getPortPosition(),
                techStacks,
                tags,
                portfolio.getPortImageUrl(),
                portfolio.getCreatedAt(),
                linkResponses,
                educationResponses,
                awardResponses,
                careerResponses
        );
    }


    public static PortfolioResponse.PortfolioListResponse toPorListResponse(Portfolio portfolio, CommunityResponse.MemberInfo member, Long likeCount) {
        List<String> tags = portfolio.getTags() != null ? portfolio.getTags() : new ArrayList<>();

        return new PortfolioResponse.PortfolioListResponse(
                portfolio.getId(),
                portfolio.getPortTitle(),
                portfolio.getPortPosition(),
                portfolio.getTags(),
                portfolio.getPortImageUrl(),
                member,
                portfolio.getCreatedAt(),
                0L,
                portfolio.getPortViews(),
                likeCount
        );

    }
}
