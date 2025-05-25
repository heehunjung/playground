package heehunjun.playground.controller.article;

import heehunjun.playground.dto.article.ArticleResponses;
import heehunjun.playground.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/api/article/search")
    public ResponseEntity<ArticleResponses> getArticleByCond(@RequestParam String cond) {
        ArticleResponses result = articleService.findByCond(cond);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/article")
    public ResponseEntity<ArticleResponses> getArticle() {
        ArticleResponses result = articleService.findAll();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/article/count")
    public ResponseEntity<Long> getArticleCount() {
        long result = articleService.countArticle();

        return ResponseEntity.ok(result);
    }

}
