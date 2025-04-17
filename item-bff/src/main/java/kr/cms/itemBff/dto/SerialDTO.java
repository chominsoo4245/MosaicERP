package kr.cms.itemBff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerialDTO {
    private Long serialId;
    private Long itemId;
    private Long lotId;
    private String serialNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}