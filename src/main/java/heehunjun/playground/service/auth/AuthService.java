package heehunjun.playground.service.auth;

import heehunjun.playground.client.auth.OAuthContext;
import heehunjun.playground.client.auth.OAuthType;
import heehunjun.playground.dto.member.MemberInfo;
import heehunjun.playground.dto.member.OauthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthService {

    private final OAuthContext oauthContext;

    public MemberInfo getMemberInfo(OAuthType type, String code) {
        OauthToken request = oauthContext.getOauthToken(type, code);
        return oauthContext.getMemberInfo(type, request);
    }
}
