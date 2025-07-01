package heehunjun.playground;

import heehunjun.playground.config.argumentResolver.MemberArgumentResolver;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@SpringBootTest
class PlaygroundApplicationTests {

	@Autowired
	private List<HandlerMethodArgumentResolver> resolvers;

	@Test
	void contextLoads() {
		for (HandlerMethodArgumentResolver resolver : resolvers) {
			if (resolver instanceof MemberArgumentResolver) {
				System.out.println("MemberArgumentResolver 구현체: " + resolver.getClass().getName());
			}
		}
	}
}
