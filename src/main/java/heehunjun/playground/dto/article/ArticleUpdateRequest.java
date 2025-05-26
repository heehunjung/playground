package heehunjun.playground.dto.article;

import heehunjun.playground.domain.article.Article;
import heehunjun.playground.domain.member.Member;
import jakarta.validation.constraints.NotNull;

public record ArticleUpdateRequest(@NotNull String title, @NotNull String content) {

    public Article toArticle(Member member) {
        return new Article(title, content, member);
    }
}
