package heehunjun.playground.dto.article;

import heehunjun.playground.domain.article.Article;
import java.util.List;
import java.util.stream.Collectors;

public record ArticleResponses(List<ArticleResponse> articleResponses) {

    public static ArticleResponses of(List<Article> articles) {
        return articles.stream()
                .map(ArticleResponse::of)
                .collect(Collectors.collectingAndThen(Collectors.toList(), ArticleResponses::new));
    }
}
