package com.DeBM.ApiDeBM.service.Interface;

import com.DeBM.ApiDeBM.dto.TokenDTO;

public interface InterfaceAutenticazione {
    boolean addUser(String username, String password, Integer idAutore);
    boolean changeUsername(String oldUsername, String newUsername);
    TokenDTO getToken(String username, String password);
    boolean invalidateToken(String token);
    TokenDTO doRefreshToken(String refreshToken);

}
