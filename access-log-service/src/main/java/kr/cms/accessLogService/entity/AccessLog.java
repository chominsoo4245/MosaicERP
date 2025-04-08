package kr.cms.accessLogService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_access_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String method;
    private String path;
    private String ip;
    private String userAgent;
    private int statusCode;
    private long responseTimeMs;
    private LocalDateTime createdAt;

}
