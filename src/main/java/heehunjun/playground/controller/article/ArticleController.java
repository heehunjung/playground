package heehunjun.playground.controller.article;

import heehunjun.playground.controller.auth.AuthMember;
import heehunjun.playground.domain.article.Article;
import heehunjun.playground.domain.member.Member;
import heehunjun.playground.dto.article.ArticleCreateRequest;
import heehunjun.playground.dto.article.ArticleResponse;
import heehunjun.playground.dto.article.ArticleResponses;
import heehunjun.playground.dto.article.ArticleUpdateRequest;
import heehunjun.playground.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/api/article")
    public ResponseEntity<Long> createArticle(@RequestBody ArticleCreateRequest request,
                                              @AuthMember Member member) {
        Article article = request.toArticle(member);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.createArticle(article));
    }

    @PutMapping("/api/article/{articleId}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long articleId,
                                                         @RequestBody ArticleUpdateRequest request,
                                                         @AuthMember Member member) {
        articleService.updateArticle(articleId, request.toArticle(member));

        return ResponseEntity.noContent().build();
    }
}
