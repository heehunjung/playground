package heehunjun.playground.dto.article;

import heehunjun.playground.domain.article.Article;
import heehunjun.playground.domain.member.Member;

public record ArticleCreateRequest(String title, String content) {

    public Article toArticle(Member member) {
        return new Article(title, content, member);
    }
}
