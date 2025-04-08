package kr.cms.common.dto;

import java.time.LocalDateTime;

public class AuditLogDto implements BaseLogDto {
    private String action;
    private String loginId;
    private String description;
    private String ip;
    private String userAgent;
    private LocalDateTime timestamp;

    public AuditLogDto() {

    }

    public AuditLogDto(String action, String loginId, String description, String ip, String userAgent, LocalDateTime timestamp) {
        this.action = action;
        this.loginId = loginId;
        this.description = description;
        this.ip = ip;
        this.userAgent = userAgent;
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
