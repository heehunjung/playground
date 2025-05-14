package heehunjun.playground.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OauthToken(@JsonProperty("id_token") String accessToken) {
}
