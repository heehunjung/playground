package heehunjun.playground.dto.member;

import heehunjun.playground.domain.member.Member;

public record MemberResponse(String email, long id) {

    public MemberResponse(Member member) {
        this(member.getEmail(), member.getId());
    }
}
