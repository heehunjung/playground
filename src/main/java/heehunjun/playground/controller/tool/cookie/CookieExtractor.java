package heehunjun.playground.controller.tool.cookie;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CookieExtractor {

    private static final String PREFIX = "Bearer ";

    public String extract(final HttpServletRequest request, final String header) {
        final String accessToken = request.getHeader(header);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(PREFIX)) {
            return accessToken.substring(PREFIX.length());
        }
        return null;
    }
}
