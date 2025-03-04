package com.backend.devfordev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomPageResponse<T> {
    private int totalPages;       // 총 페이지 수
    private long totalElements;   // 총 데이터 개수
    private int pageNumber;       // 현재 페이지 번호
    private int pageSize;         // 페이지 크기
    private boolean first;        // 첫 번째 페이지 여부
    private boolean last;         // 마지막 페이지 여부
    private List<T> content;      // 데이터 리스트

    // Page 객체를 받아서 변환하는 생성자
    public CustomPageResponse(Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.content = page.getContent();
    }
}
