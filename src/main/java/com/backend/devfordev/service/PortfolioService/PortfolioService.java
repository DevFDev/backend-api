package com.backend.devfordev.service.PortfolioService;

import com.backend.devfordev.dto.PortfolioDto.PortfolioRequest;
import com.backend.devfordev.dto.PortfolioDto.PortfolioResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PortfolioService {
    public PortfolioResponse.PortCreateResponse createPortfolio(PortfolioRequest.PortfolioCreateRequest request, Long userId, MultipartFile portImage);
    public List<PortfolioResponse.PortfolioListResponse> getPortList(String position, Optional<String> searchTermOpt, String sortBy);

    public void deletePortfolio(Long portId, Long userId);
    public PortfolioResponse.PortDetailResponse getPortfolioDetail(Long portfolioId);
}
