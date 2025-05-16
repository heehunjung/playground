package heehunjun.playground.service.article;

import heehunjun.playground.domain.article.Article;
import heehunjun.playground.dto.article.ArticleResponse;
import heehunjun.playground.dto.article.ArticleResponses;
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
    public ArticleResponses findByTitle(String title) {
        List<Article> articles = articleRepository.findAll();

        return ArticleResponses.of(articles);
    }

    @Transactional(readOnly = true)
    public ArticleResponses findAll() {
        List<Article> articles = articleRepository.findAll();

        return ArticleResponses.of(articles);
    }
}
