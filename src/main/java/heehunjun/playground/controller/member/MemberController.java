package heehunjun.playground.controller.member;

import heehunjun.playground.controller.tool.cookie.CookieManager;
import heehunjun.playground.controller.tool.token.jwt.JwtManager;
import heehunjun.playground.dto.member.MemberInfo;
import heehunjun.playground.dto.member.MemberRequest;
import heehunjun.playground.dto.member.MemberResponse;
import heehunjun.playground.service.auth.AuthService;
import heehunjun.playground.service.member.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class MemberController {

    private final AuthService authService;
    private final JwtManager jwtManager;
    private final CookieManager cookieManager;
    private final MemberService memberService;

    @GetMapping("/api/member")
    public ResponseEntity<MemberResponse> login(@RequestBody MemberRequest request) {
        MemberInfo memberInfo = authService.getMemberInfo(request.type(), request.code());
        String accessToken = jwtManager.generateAccessToken(memberInfo.email());
        String refreshToken = jwtManager.generateRefreshToken(memberInfo.email());
        ResponseCookie responseCookie = cookieManager.generateRefreshToken(refreshToken);
        MemberResponse result = memberService.createMember(memberInfo, request.type().name());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(result);
    }
}
