package heehunjun.playground.service.article;

import static org.assertj.core.api.Assertions.*;

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
    class updateArticle {

        @Test
        void concurrentUpdateTest() throws InterruptedException {
            // given
            Member member = new Member("testUser", "test@test.com", "kakao");
            memberRepository.save(member);
            Article article = new Article("a".repeat(20), "a".repeat(25), member);
            Article savedArticle = articleRepository.save(article);
            ArticleUpdateRequest request = new ArticleUpdateRequest("b".repeat(20),
                    "b".repeat(25));

            // when
            int threadCount = 1000;
            ExecutorService executor = Executors.newFixedThreadPool(100);
            CountDownLatch latch = new CountDownLatch(threadCount);
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        articleService.updateArticle(savedArticle, request.toArticle(member));
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            articleService.updateArticle(savedArticle, request.toArticle(member));
            Article updatedArticle = articleRepository.findById(savedArticle.getId()).get();
            assertThat(updatedArticle.getUpdatedCount()).isEqualTo(threadCount);
        }
    }
}
