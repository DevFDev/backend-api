package com.backend.devfordev.dto.PortfolioDto;


import com.backend.devfordev.domain.enums.AwardType;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PortfolioResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortCreateResponse {
        @Schema(description = "커뮤니티 ID", example = "1")
        Long id;
        @Schema(description = "작성자 ID", example = "1")
        Long writer;
        @Schema(description = "포트폴리오 제목", example = "김민지의 포트폴리오~~")
        String portTitle;
        @Schema(description = "포트폴리오 내용", example = "포트폴리오 내용~~")
        String portContent;
        @Schema(description = "포지션", example = "포지션")
        String portPosition;
        @ArraySchema(
                schema = @Schema(description = "기술 스택", example = "스택1"),
                arraySchema = @Schema(example = "[\"스택1\", \"스택2\", \"스택3\", \"스택4\"]")
        )
        List<String> techStacks;
        @ArraySchema(
                schema = @Schema(description = "태그", example = "태그1"),
                arraySchema = @Schema(example = "[\"태그1\", \"태그2\", \"태그3\", \"태그4\"]")
        )
        List<String> tags;
        @Schema(description = "포트폴리오 이미지 url", example = "이미지url")
        String portImageUrl;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "포트폴리오 링크 리스트")
        private List<LinkResponse> links; // 링크 리스트 추가
        @Schema(description = "학력 리스트")
        private List<EducationResponse> educations;
        @Schema(description = "수상 및 기타 정보 리스트")
        private List<AwardResponse> awards; // 수상 및 기타 정보 리스트
        @Schema(description = "경력 리스트")
        private List<CareerResponse> careers; // 수상 및 기타 정보 리스트

        @Getter
        @Setter
        public static class LinkResponse {
            @Schema(description = "링크 타입", example = "github")
            private String type;
            @Schema(description = "url", example = "https://github.com/bboggo")
            private String url;
            @Schema(description = "정렬 순서", example = "1")
            private Integer orderIndex;


            public LinkResponse(String type, String url, Integer orderIndex) {
                this.type = type;
                this.url = url;
                this.orderIndex = orderIndex;
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class EducationResponse {
            @Schema(description = "포트폴리오-학력 id", example = "1")
            private Long id;
            @Schema(description = "학력구분", example = "대학(2,3년제)")
            private String level;
            @Schema(description = "학교명", example = "명지전문대학")
            private String institutionName;
            @Schema(description = "전공", example = "정보통신공학과")
            private String major;
            @Schema(description = "입학일", example = "2020-03-02")
            private LocalDate admissionDate;
            @Schema(description = "졸업일", example = "2024-02-27")
            private LocalDate graduationDate;
            @Schema(description = "졸업여부선택", example = "졸업")
            private String graduationStatus;
            @Schema(description = "편입여부", example = "false")
            private Boolean isTransfer;
            @Schema(description = "학점", example = "4.25")
            private Double grade;
            @Schema(description = "기준학점", example = "4.5")
            private Double gradeScale;
            @Schema(description = "정렬 순서", example = "1")
            private Integer orderIndex; // 순서
        }


        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AwardResponse {
            @Schema(description = "포트폴리오-수상 및 기타 활동 id", example = "1")
            private Long id;
            @Schema(description = "수상/자격증/어학/대외활동 유형", example = "COMPETITION")
            private AwardType awardType;
            @Schema(description = "정렬 순서", example = "1")
            private Integer orderIndex;



            @Getter
            @Setter
            public static class CompetitionAwardResponse extends AwardResponse {
                // CompetitionAward 관련 필드
                @Schema(description = "수상 및 공모전명", example = "정보통신공학과 학술제")
                private String competitionName;
                @Schema(description = "주최기관", example = "명지전문대학 정보통신공학과")
                private String hostingInstitution;
                @Schema(description = "공모일", example = "2023-11-01")
                private LocalDate competitionDate;
                // 기본 생성자
                public CompetitionAwardResponse() {
                    super(); // 부모 클래스의 기본 생성자 호출
                }
                public CompetitionAwardResponse(
                        Long id,
                        AwardType awardType,
                        Integer orderIndex,
                        String competitionName,
                        String hostingInstitution,
                        LocalDate competitionDate
                ) {
                    super(id, awardType, orderIndex); // 부모 클래스의 생성자 호출
                    this.competitionName = competitionName;
                    this.hostingInstitution = hostingInstitution;
                    this.competitionDate = competitionDate;
                }
            }


            @Getter
            @Setter
            public static class CertificateAwardResponse extends AwardResponse {
                // CertificateAward 관련 필드
                @Schema(description = "자격증명", example = "정보처리기사")
                private String certificateName;
                @Schema(description = "발행처", example = "한국산업인력공단")
                private String issuer;
                @Schema(description = "합격년월", example = "2023-00")
                private LocalDate passingDate;

                // 기본 생성자
                public CertificateAwardResponse() {
                    super(); // 부모 클래스의 기본 생성자 호출
                }
                public CertificateAwardResponse(
                        Long id,
                        AwardType awardType,
                        Integer orderIndex,
                        String certificateName,
                        String issuer,
                        LocalDate passingDate
                ) {
                    super(id, awardType, orderIndex);
                    this.certificateName = certificateName;
                    this.issuer = issuer;
                    this.passingDate = passingDate;
                }
            }

            @Getter
            @Setter
            public static class LanguageAwardResponse extends AwardResponse {
                // LanguageAward 관련 필드
                @Schema(description = "언어", example = "영어")
                private String language;
                @Schema(description = "시험명", example = "TOEIC")
                private String testName;
                @Schema(description = "점수", example = "900")
                private String score;
                @Schema(description = "취득일", example = "2022-08-15")
                private LocalDate obtainedDate;
                public LanguageAwardResponse() {
                    super(); // 부모 클래스의 기본 생성자 호출
                }
                public LanguageAwardResponse(
                        Long id,
                        AwardType awardType,
                        Integer orderIndex,
                        String language,
                        String testName,
                        String score,
                        LocalDate obtainedDate
                ) {
                    super(id, awardType, orderIndex);
                    this.language = language;
                    this.testName = testName;
                    this.score = score;
                    this.obtainedDate = obtainedDate;
                }
            }


            @Getter
            @Setter
            public static class ActivityAwardResponse extends AwardResponse {
                // ActivityAward 관련 필드
                @Schema(description = "활동명", example = "글로벌 리더십 프로그램")
                private String activityName;
                @Schema(description = "활동 시작일", example = "2024-08-01")
                private LocalDate startDate;
                @Schema(description = "활동 종료일", example = "2024-11-30")
                private LocalDate endDate;

                public ActivityAwardResponse() {
                    super(); // 부모 클래스의 기본 생성자 호출
                }
                public ActivityAwardResponse(
                        Long id,
                        AwardType awardType,
                        Integer orderIndex,
                        String activityName,
                        LocalDate startDate,
                        LocalDate endDate
                ) {
                    super(id, awardType, orderIndex);
                    this.activityName = activityName;
                    this.startDate = startDate;
                    this.endDate = endDate;
                }
            }




//            public AwardResponse(String awardType, Integer orderIndex, String certificateName, String issuer, Integer passingYear) {
//                this.awardType = awardType;
//                this.orderIndex = orderIndex;
//                this.certificateName = certificateName;
//                this.issuer = issuer;
//                this.passingYear = passingYear;
//            }
//
//            public AwardResponse(String awardType, Integer orderIndex, String language, String level, String testName, String score, LocalDate obtainedDate) {
//                this.awardType = awardType;
//                this.orderIndex = orderIndex;
//                this.language = language;
//                this.level = level;
//                this.testName = testName;
//                this.score = score;
//                this.obtainedDate = obtainedDate;
//            }
//
//            public AwardResponse(String awardType, Integer orderIndex, String activityName, LocalDate startDate, LocalDate endDate, String description) {
//                this.awardType = awardType;
//                this.orderIndex = orderIndex;
//                this.activityName = activityName;
//                this.startDate = startDate;
//                this.endDate = endDate;
//                this.description = description;
//            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CareerResponse {
            @Schema(description = "경력 ID", example = "1")
            private Long id;

            @Schema(description = "회사명", example = "Google")
            private String companyName;

            @Schema(description = "정렬 순서", example = "1")
            private Integer orderIndex;

            @Schema(description = "직위", example = "백엔")
            private String position;

            @Schema(description = "입사일", example = "2024-08-01")
            private LocalDate startDate;

            @Schema(description = "퇴사일", example = "2024-08-01")
            private LocalDate endDate;

            @Schema(description = "재직 중 여부", example = "true")
            private Boolean isCurrent;

            @Schema(description = "주요 업무", example = "Backend 개발 및 데이터베이스 설계")
            private String description;
        }

    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioListResponse {
        @Schema(description = "커뮤니티 ID", example = "1")
        Long id;
        @Schema(description = "포트폴리오 제목", example = "김민지의 포트폴리오~~")
        String portTitle;
        @Schema(description = "포지션", example = "포지션")
        String portPosition;
        @ArraySchema(
                schema = @Schema(description = "태그", example = "태그1"),
                arraySchema = @Schema(example = "[\"태그1\", \"태그2\", \"태그3\", \"태그4\"]")
        )
        private List<String> tags;
        @Schema(description = "포트폴리오 이미지 url", example = "이미지url")
        String portImageUrl;
        CommunityResponse.MemberInfo writer;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "답변수", example = "0")
        Long answers;
        @Schema(description = "조회수", example = "0")
        Long views;
        @Schema(description = "좋아요수", example = "0")
        Long likes;
    }
}
