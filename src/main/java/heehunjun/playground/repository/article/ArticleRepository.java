package heehunjun.playground.repository.article;

import heehunjun.playground.domain.article.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository  extends JpaRepository<Article, Long> {

    //간단하게
    @Query("SELECT a FROM Article a WHERE a.title = :cond")
    List<Article> findByCond(@Param("cond") String cond);  // <- 구현체 ArrayList

    @Query("SELECT COUNT(1) FROM Article")
    long myCount();
}
