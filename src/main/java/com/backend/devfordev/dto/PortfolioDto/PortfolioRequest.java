package com.backend.devfordev.dto.PortfolioDto;

import com.backend.devfordev.domain.enums.AwardType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class PortfolioRequest {

    @Getter
    @Setter
    public static class PortfolioCreateRequest {
        @NotNull(message = "This field must not be null.")
        @Schema(description = "포트폴리오 제목", example = "김민지의 포트폴리오~~")
        String portTitle;
        @NotNull(message = "This field must not be null.")
        @Schema(description = "포트폴리오 내용", example = "마크다운 텍스트 부분")
        String portContent;
        @NotNull(message = "This field must not be null.")
        @Schema(description = "백엔드", example = "포지션")
        String portPosition;
        @NotNull(message = "This field must not be null.")
        @Schema(description = "기술 스택", example = "[\"Spring\", \"Java\", \"MySQL\", \"Docker\"]")
        private List<String> techStacks;
        @NotNull(message = "This field must not be null.")
        @ArraySchema(
                schema = @Schema(description = "태그", example = "태그1"),
                arraySchema = @Schema(example = "[\"태그1\", \"태그2\", \"태그3\", \"태그4\"]")
        )
        private List<String> tags;


        @Schema(description = "포트폴리오 링크 리스트")
        private List<LinkRequest> links; // 링크 리스트 추가
        @Schema(description = "포트폴리오 학력 리스트")
        private List<EducationRequest> educations;
        @ArraySchema(
                schema = @Schema(description = "포트폴리오 수상 및 기타 정보 리스트", implementation = AwardRequest.class),
                arraySchema = @Schema(
                        example = """
            [
              {
                "awardType": "ACTIVITY",
                "activityName": "글로벌 리더십 프로그램",
                "startDate": "2024-08-01",
                "endDate": "2024-11-30"
              },
              {
                "awardType": "CERTIFICATE",
                "certificateName": "정보처리기사",
                "issuer": "한국산업인력공단",
                "passingDate": "2024-11-25"
              },
              {
                "awardType": "COMPETITION",
                "competitionName": "정보통신공학과 학술제",
                "hostingInstitution": "명지전문대학 정보통신공학과",
                "competitionDate": "2023-11-01"
              },
              {
                "awardType": "LANGUAGE",
                "language": "영어",
                "testName": "TOEIC",
                "score": "900",
                "obtainedDate": "2022-08-15"
              }
            ]
            """
                )
        )
        private List<AwardRequest> awards; // 수상 및 기타 정보 추가
        @Schema(description = "포트폴리오 경력 리스트")
        private List<CareerRequest> careers; // 경력

        @Getter
        @Setter
        public static class LinkRequest {

            @Schema(description = "링크 타입", example = "github")
            private String type;
            @Schema(description = "url", example = "https://github.com/bboggo")
            private String url;

        }


        @Getter
        @Setter
        @AllArgsConstructor
        public static class EducationRequest {
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
            private String isTransfer;

            @Schema(description = "학점", example = "4.25")
            private Double grade;

            @Schema(description = "기준학점", example = "4.5")
            private Double gradeScale;
        }


        @Getter
        @Setter
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "awardType")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = AwardRequest.CompetitionAwardRequest.class, name = "COMPETITION"),
                @JsonSubTypes.Type(value = AwardRequest.CertificationAwardRequest.class, name = "CERTIFICATE"),
                @JsonSubTypes.Type(value = AwardRequest.LanguageAwardRequest.class, name = "LANGUAGE"),
                @JsonSubTypes.Type(value = AwardRequest.ActivityAwardRequest.class, name = "ACTIVITY")
        })
        @Schema(description = "수상/자격증/어학/대외활동 Request")
        public static class AwardRequest {

            private AwardType awardType;

            @Getter
            @Setter
            @Schema(description = "공모전 관련 정보")
            public static class CompetitionAwardRequest extends AwardRequest {
                @Schema(description = "수상 및 공모전명", example = "정보통신공학과 학술제")
                private String competitionName;

                @Schema(description = "주최기관", example = "명지전문대학 정보통신공학과")
                private String hostingInstitution;

                @Schema(description = "공모일", example = "2023-11-01")
                private LocalDate competitionDate;
            }

            @Getter
            @Setter
            @Schema(description = "자격증 관련 정보")
            public static class CertificationAwardRequest extends AwardRequest {
                @Schema(description = "자격증명", example = "정보처리기사")
                private String certificateName;

                @Schema(description = "발행처", example = "한국산업인력공단")
                private String issuer;

                @Schema(description = "합격년월", example = "2024-11-25")
                private LocalDate passingDate;
            }

            @Getter
            @Setter
            @Schema(description = "어학 관련 정보")
            public static class LanguageAwardRequest extends AwardRequest {
                @Schema(description = "언어", example = "영어")
                private String language;

                @Schema(description = "시험명", example = "TOEIC")
                private String testName;

                @Schema(description = "점수", example = "900")
                private String score;

                @Schema(description = "취득일", example = "2022-08-15")
                private LocalDate obtainedDate;
            }

            @Getter
            @Setter
            @Schema(description = "대외활동 관련 정보")
            public static class ActivityAwardRequest extends AwardRequest {
                @Schema(description = "활동명", example = "글로벌 리더십 프로그램")
                private String activityName;

                @Schema(description = "활동 시작일", example = "2024-08-01")
                private LocalDate startDate;

                @Schema(description = "활동 종료일", example = "2024-11-30")
                private LocalDate endDate;

            }
        }


        @Getter
        @Setter
        public static class CareerRequest {

            @Schema(description = "회사명", example = "oo회사")
            private String companyName;

            @Schema(description = "포지션", example = "백엔드")
            private String position;

            @Schema(description = "입사일", example = "2024-08-01")
            private LocalDate startDate;

            @Schema(description = "퇴사일", example = "2024-08-01")
            private LocalDate endDate;

            @Schema(description = "재직여부", example = "true")
            private Boolean isCurrent;

            @Schema(description = "직급(직위)", example = "과장")
            private String level;

            @Schema(description = "업무 설명", example = "업무 설명 내용")
            private String description;

        }
    }

}
