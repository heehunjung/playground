package heehunjun.playground.service.article;

import heehunjun.playground.domain.article.Article;
import heehunjun.playground.dto.article.ArticleResponse;
import heehunjun.playground.dto.article.ArticleResponses;
import heehunjun.playground.dto.article.ArticleUpdateRequest;
import heehunjun.playground.exception.HhjClientException;
import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.repository.article.ArticleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public ArticleResponses findByCond(String cond) {
        List<Article> articles = articleRepository.findByCond(cond);

        return ArticleResponses.of(articles);
    }

    @Transactional(readOnly = true)
    public ArticleResponses findAll() {
        List<Article> articles = articleRepository.findAll();

        return ArticleResponses.of(articles);
    }

    @Transactional(readOnly = true)
    public long countArticle() {
        return articleRepository.myCount();
    }

    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new HhjClientException(ClientErrorCode.ARTICLE_NOT_FOUND));
    }

    @Transactional
    public Long createArticle(Article article) {
        return articleRepository.save(article)
                .getId();
    }

    @Transactional
    public ArticleResponse updateArticle(long articleId, Article updatedArticle) {
        Article article = articleRepository.findById(articleId)
                        .orElseThrow(() -> new HhjClientException(ClientErrorCode.ARTICLE_NOT_FOUND));
        article.update(updatedArticle);

        return ArticleResponse.of(articleRepository.save(article));
    }
}
