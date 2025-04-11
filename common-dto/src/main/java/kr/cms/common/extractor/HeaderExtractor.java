package kr.cms.common.extractor;

import kr.cms.common.annotation.RequestHeaders;
import kr.cms.common.provider.HeaderProvider;

import java.lang.reflect.Field;

public class HeaderExtractor {
    public static <T> T extractHeaders(HeaderProvider provider, Class<T> dtoClass) {
        try {
            T dto = dtoClass.newInstance();

            for(Field field : dtoClass.getDeclaredFields()) {
                RequestHeaders annotation = field.getAnnotation(RequestHeaders.class);
                if (annotation != null) {
                    String headerName = annotation.value();
                    if (headerName.isEmpty()) {
                        headerName = field.getName();
                    }

                    String headerValue = provider.getHeader(headerName);
                    if ( headerValue != null) {
                        field.setAccessible(true);
                        field.set(dto, headerValue);
                    }
                }
            }
            
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("헤더 추출 중 오류 발생", e);
        }
    }
}
