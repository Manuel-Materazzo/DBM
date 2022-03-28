package com.DeBM.ApiDeBM.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RegisterDTO {
    String email;
    String password;
    String nomeArte;
    String confirmPassword;
    boolean terms;
}
