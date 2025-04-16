package kr.cms.serialService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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
