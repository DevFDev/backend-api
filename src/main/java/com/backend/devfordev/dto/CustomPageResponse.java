package com.backend.devfordev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class CustomPageResponse<T> {
    private List<T> content;          // 실제 데이터
    private int totalPages;           // 전체 페이지 수
    private long totalElements;       // 전체 데이터 수
    private int currentPage;          // 현재 페이지 번호
    private int size;                 // 페이지 크기
    private boolean isFirst;          // 첫 번째 페이지 여부
    private boolean isLast;           // 마지막 페이지 여부

    public CustomPageResponse(List<T> content, int totalPages, long totalElements, int currentPage, int size, boolean first, boolean last) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage + 1; // 1 기반으로 변환
        this.size = size;
        this.isFirst = first;
        this.isLast = last;
    }
}
