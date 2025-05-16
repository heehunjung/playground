package heehunjun.playground.repository.article;

import heehunjun.playground.domain.article.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository  extends JpaRepository<Article, Long> {

    public List<Article> findByTitle(String title);
}
