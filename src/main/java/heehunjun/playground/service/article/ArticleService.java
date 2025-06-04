package heehunjun.playground.service.article;

import heehunjun.playground.domain.article.Article;
import heehunjun.playground.dto.article.ArticleResponse;
import heehunjun.playground.dto.article.ArticleResponses;
import heehunjun.playground.dto.article.ArticleUpdateRequest;
import heehunjun.playground.exception.HhjClientException;
import heehunjun.playground.exception.code.ClientErrorCode;
import heehunjun.playground.repository.article.ArticleRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
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
    public void updateArticle(long articleId, Article updatedArticle) {
//        Article article = articleRepository.findById(articleId)
        Article article = articleRepository.findByIdWithPessimisticLock(articleId)
                .orElseThrow(() -> new HhjClientException(ClientErrorCode.ARTICLE_NOT_FOUND));
        article.update(updatedArticle);
    }

    @Transactional
    public void updateArticleWithOptimisticLock(long articleId, Article updatedArticle)
            throws InterruptedException {
        while (true) {
            try {
                Article article = articleRepository.findByIdWithOptimisticLock(articleId)
                        .orElseThrow(
                                () -> new HhjClientException(ClientErrorCode.ARTICLE_NOT_FOUND));

                article.update(updatedArticle);
                articleRepository.saveAndFlush(article); //Flush 되는 시점에서 낙관적 락 검사
                // 이전에 메서드 분리했을 땐 내부 메서드 호출이라 transaction이 안먹혔던 것
                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("Optimistic lock failure");
                Thread.sleep(50);
            }
        }
    }
}
