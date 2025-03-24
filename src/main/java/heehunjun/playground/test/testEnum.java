package heehunjun.playground.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum testEnum {

    TEST_1("테스트1", "100"),
    TEST_2("테스트2", "200");

    private final String message;
    private final String code;

}
