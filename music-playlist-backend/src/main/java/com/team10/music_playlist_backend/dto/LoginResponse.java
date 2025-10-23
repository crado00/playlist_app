package com.team10.music_playlist_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team10.music_playlist_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    @JsonProperty // ✅ JSON 응답을 프론트 이름에 맞춤
    private String accessToken;

    @JsonProperty // ✅ 나중 확장용
    private String refreshToken;

    private UserDto user;

    public LoginResponse(String accessToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = ""; // 현재 리프레시 토큰이 없으면 빈 문자열
        this.user = new UserDto(user);
    }
}