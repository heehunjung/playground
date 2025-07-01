package heehunjun.playground.dto.member;

import heehunjun.playground.client.auth.OAuthType;

public record MemberRequest(String code, OAuthType type) {
}
