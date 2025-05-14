package heehunjun.playground.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberInfo(@JsonProperty("nickName") String name, String email) {
}
