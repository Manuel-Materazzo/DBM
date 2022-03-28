package com.DeBM.ApiDeBM.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoginDTO {
    String email;
    String password;
}
