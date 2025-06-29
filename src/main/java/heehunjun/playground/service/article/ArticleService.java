package heehunjun.playground.service.article;

import heehunjun.playground.domain.article.Article;
import heehunjun.playground.dto.article.ArticleResponses;
import heehunjun.playground.exception.HhjClientException;
import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.repository.article.ArticleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import java.sql.Connection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final EntityManager entityManager;

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
    public void updateArticle(long articleId, Article updatedArticle) {
//        Article article = articleRepository.findById(articleId)
        Article article = articleRepository.findByIdWithPessimisticLock(articleId)
                .orElseThrow(() -> new HhjClientException(ClientErrorCode.ARTICLE_NOT_FOUND));
        article.update(updatedArticle);
    }

    @Retryable(
            value = {OptimisticLockException.class, ObjectOptimisticLockingFailureException.class},
            maxAttempts = 200,
            backoff = @Backoff(delay = 10)
    )
    @Transactional
    public void updateArticleWithOptimisticLock(long articleId, Article updatedArticle) {
        Article article = articleRepository.findByIdWithOptimisticLock(articleId)
                .orElseThrow(() -> new HhjClientException(ClientErrorCode.ARTICLE_NOT_FOUND));
        entityManager.unwrap(Session.class).doWork(connection -> {
            log.info("🔁 Transaction Connection ID: {}, {}", System.identityHashCode(connection),
                    article.getVersion());
        });
        article.update(updatedArticle);
    }
}

