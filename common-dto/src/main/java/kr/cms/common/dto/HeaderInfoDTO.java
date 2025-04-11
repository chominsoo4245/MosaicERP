package kr.cms.common.dto;

import kr.cms.common.annotation.RequestHeaders;

public class HeaderInfoDTO {
    @RequestHeaders("X-Forwarded-For")
    private String ip;

    @RequestHeaders("X-User-Agent")
    private String userAgent;

    @RequestHeaders("X-User-Id")
    private String loginId;

    public String getIp() {
        if(ip != null && ip.contains(",")) {
            return ip.split(",")[0];
        }
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

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
