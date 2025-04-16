package kr.cms.lotService.provider;

import jakarta.servlet.http.HttpServletRequest;
import kr.cms.common.provider.HeaderProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SpringHeaderProvider implements HeaderProvider {

    private final HttpServletRequest request;

    public SpringHeaderProvider() {
        this.request = null;
    }

    @Override
    public String getHeader(String name) {
        HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return currentRequest.getHeader(name);
    }
}

