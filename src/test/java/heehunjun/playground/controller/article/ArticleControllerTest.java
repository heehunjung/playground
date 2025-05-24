package heehunjun.playground.controller.article;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ArticleControllerTest {

    @LocalServerPort
    private int randomPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort;
    }

    @Test
    void apiTime() {
        Map<String, String> params = new HashMap<>();
        params.put("cond", "제목-4646");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .queryParams(params)
                .contentType(ContentType.JSON)
                .get("/api/article/search")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(200);
    }
}
