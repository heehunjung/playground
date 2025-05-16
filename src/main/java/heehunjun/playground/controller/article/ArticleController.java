package heehunjun.playground.controller.article;

import heehunjun.playground.dto.article.ArticleResponse;
import heehunjun.playground.dto.article.ArticleResponses;
import heehunjun.playground.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/api/article")
    public ResponseEntity<ArticleResponses> getArticleByTitle(@RequestParam String title) {
        ArticleResponses result = articleService.findByTitle(title);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/article")
    public ResponseEntity<ArticleResponses> getArticle() {
        ArticleResponses result = articleService.findAll();

        return ResponseEntity.ok(result);
    }
}
