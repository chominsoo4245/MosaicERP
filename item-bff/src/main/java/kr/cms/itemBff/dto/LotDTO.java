package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotDTO {
    private Long lotId;
    private Long itemId;
    private String lotNumber;
    private LocalDate expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}