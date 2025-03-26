package heehunjun.playground.repository.token;

import heehunjun.playground.domain.member.Member;
import heehunjun.playground.domain.token.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface  TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByRefreshToken(final String refreshToken);

    Optional<Token> findByMember(final Member member);

    @Query("DELETE FROM Token t WHERE t.member.id = :memberId")
    @Modifying
    void deleteByMember(final Long memberId);
}
