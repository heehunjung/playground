package heehunjun.playground.service.auth;

import heehunjun.playground.client.auth.OAuthContext;
import heehunjun.playground.client.auth.OAuthType;
import heehunjun.playground.domain.member.Member;
import heehunjun.playground.dto.member.MemberInfo;
import heehunjun.playground.dto.member.MemberResponse;
import heehunjun.playground.dto.member.OauthToken;
import heehunjun.playground.exception.HhjClientException;
import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final OAuthContext oauthContext;

    public MemberInfo getMemberInfo(OAuthType type, String code) {
        OauthToken request = oauthContext.getOauthToken(type, code);

        return oauthContext.getMemberInfo(type, request);
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new HhjClientException(ClientErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(()-> new HhjClientException(ClientErrorCode.MEMBER_NOT_FOUND));
    } // 없으면 ERROR OR 없다 ?
}
