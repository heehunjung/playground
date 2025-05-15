package heehunjun.playground.service.member;

import heehunjun.playground.domain.member.Member;
import heehunjun.playground.dto.member.MemberInfo;
import heehunjun.playground.dto.member.MemberResponse;
import heehunjun.playground.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse createMember(MemberInfo memberInfo, String type) {
        Member member = memberRepository.findByEmail(memberInfo.email())
                .orElseGet(() -> memberRepository.save(
                        new Member(memberInfo.name(), memberInfo.email(), type)));

        return new MemberResponse(member);
    }
}
