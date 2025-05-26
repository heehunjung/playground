package heehunjun.playground.domain.article;

import heehunjun.playground.domain.member.Member;
import heehunjun.playground.exception.HhjClientException;
import heehunjun.playground.exception.code.ClientErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Article(String title, String content, Member member) {
        validateTitle(title);
        validateContent(content);
        this.title = title;
        this.content = content;
        this.member = member;
    }

    private void validateTitle(String title) {
        if (title.length() < 3 || title.length() > 25) {
            throw new HhjClientException(ClientErrorCode.INVALID_TITLE_LENGTH);
        }
    }

    private void validateContent(String content) {
        if (content.length() < 20 || content.length() > 120) {
            throw new HhjClientException(ClientErrorCode.INVALID_CONTENT_LENGTH);
        }
    }
}
