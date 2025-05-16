package heehunjun.playground.dto.article;

import heehunjun.playground.domain.article.Article;

public record ArticleResponse(String title, String content) {

    public static ArticleResponse of(Article article) {
        return new ArticleResponse(article.getTitle(), article.getContent());
    }
}
