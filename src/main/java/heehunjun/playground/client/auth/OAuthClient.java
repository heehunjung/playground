package heehunjun.playground.client.auth;

import heehunjun.playground.dto.member.MemberInfo;
import heehunjun.playground.dto.member.OauthToken;

public interface OAuthClient {

    OauthToken getOauthToken(String code);

    MemberInfo getMemberInfo(String token);
}
