package kr.cms.lotService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotDTO {
    private Long lotId;
    private Long itemId;
    private String lotNumber;
    private Integer initialStock;
    private LocalDateTime expirationDate;
    private String locationInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
