package com.DeBM.ApiDeBM.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {
    TokenObject token;

    public TokenDTO(String access_token, String refresh_token){
        token = TokenObject.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
   public static class TokenObject {
        String access_token;
        String refresh_token;
    }
}

