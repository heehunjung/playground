package heehunjun.playground.service.article;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import heehunjun.playground.domain.article.Article;
import heehunjun.playground.domain.member.Member;
import heehunjun.playground.dto.article.ArticleUpdateRequest;
import heehunjun.playground.repository.article.ArticleRepository;
import heehunjun.playground.repository.member.MemberRepository;
import io.restassured.RestAssured;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ArticleServiceTest {

    @LocalServerPort
    private int randomPort;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort;
    }

    @Nested
    class UpdateArticle {

        @Test
        void concurrentUpdateTest_비관적락() throws InterruptedException {
            Member member = new Member("testUser", "test@test.com", "kakao");
            memberRepository.save(member);
            Article article = new Article("a".repeat(20), "a".repeat(25), member);
            Article savedArticle = articleRepository.save(article);
            ArticleUpdateRequest request = new ArticleUpdateRequest("b".repeat(20), "b".repeat(25));
            int threadCount = 2000;

            runConcurrentUpdate(
                    () -> articleService.updateArticle(savedArticle.getId(),
                            request.toArticle(member)),
                    threadCount
            );

            validate(savedArticle, threadCount, request);
        }

        @Test
        void concurrentUpdateTest_낙관적락() throws InterruptedException {
            Member member = new Member("testUser", "test@test.com", "kakao");
            memberRepository.save(member);
            Article article = new Article("a".repeat(20), "a".repeat(25), member);
            Article savedArticle = articleRepository.save(article);
            ArticleUpdateRequest request = new ArticleUpdateRequest("b".repeat(20), "b".repeat(25));
            int threadCount = 200;

            runConcurrentUpdate(
                    () -> {
                        articleService.updateArticleWithOptimisticLock(savedArticle.getId(),
                                request.toArticle(member));
                    },
                    threadCount
            );
            validate(savedArticle, threadCount, request);
        }

        private void runConcurrentUpdate(Runnable task, int threadCount)
                throws InterruptedException {
            ExecutorService executor = Executors.newFixedThreadPool(100);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        task.run();
                    } finally{
                        latch.countDown();
                    }
                });
            }

            latch.await();
        }
    }

    private void validate(Article savedArticle, int threadCount, ArticleUpdateRequest request) {
        Article updatedArticle = articleRepository.findById(savedArticle.getId()).orElseThrow();
        assertAll(
                () -> assertThat(updatedArticle.getUpdatedCount()).isEqualTo(threadCount),
                () -> assertThat(updatedArticle.getTitle()).isEqualTo(request.title()),
                () -> assertThat(updatedArticle.getContent()).isEqualTo(request.content())
        );
    }
}
