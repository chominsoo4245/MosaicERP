package kr.cms.common.dto;

import java.time.LocalDateTime;

public class AccessLogDTO implements BaseLogDTO {
    private String method;
    private String path;
    private String ip;
    private String userAgent;
    private int statusCode;
    private long responseTimeMs;
    private LocalDateTime timestamp;

    public AccessLogDTO(){

    }

    public AccessLogDTO(String method, String path, String ip, String userAgent, int statusCode, long responseTimeMs, LocalDateTime timestamp) {
        this.method = method;
        this.path = path;
        this.ip = ip;
        this.userAgent = userAgent;
        this.statusCode = statusCode;
        this.responseTimeMs = responseTimeMs;
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
