package com.backend.devfordev.service.PortfolioService;

import com.backend.devfordev.dto.CustomPageResponse;
import com.backend.devfordev.dto.PortfolioDto.PortfolioRequest;
import com.backend.devfordev.dto.PortfolioDto.PortfolioResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PortfolioService {
    public PortfolioResponse.PortCreateResponse createPortfolio(PortfolioRequest.PortfolioCreateRequest request, Long userId, MultipartFile portImage);
    @Transactional(readOnly = true)
    public CustomPageResponse<PortfolioResponse.PortfolioListResponse> getPortList(
            String position,
            Optional<String> searchTermOpt,
            String sortBy,
            Pageable pageable
    );
    public void deletePortfolio(Long portId, Long userId);
    public PortfolioResponse.PortDetailResponse getPortfolioDetail(Long portfolioId);
    public PortfolioResponse.PortCreateResponse updatePortfolio(Long portfolioId, PortfolioRequest.PortfolioCreateRequest request,  Long userId, MultipartFile portImage);
}
