package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.dto.TokenDTO;
import com.DeBM.ApiDeBM.service.Interface.InterfaceAutenticazione;
import lombok.var;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceAutenticazione implements InterfaceAutenticazione {

    private static final String SERVER_URL = "http://localhost:8090/auth";
    private static final String REALM = "DMB";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final String CLIENT_ID = "api-server";
    private final Keycloak client;

    ServiceAutenticazione(){
        client = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM)
                .username(USERNAME)
                .password(PASSWORD)
                .clientId(CLIENT_ID)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }

    public boolean addUser(String username, String password, Integer idAutore) {

        try {
            CredentialRepresentation credentials = new CredentialRepresentation();
            credentials.setType(CredentialRepresentation.PASSWORD);
            credentials.setValue(password);

            ArrayList<String> ids = new ArrayList<>();
            ids.add(idAutore.toString());

            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("idArtista", ids);

            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(username);
            userRepresentation.setEnabled(true);
            userRepresentation.setAttributes(attributes);
            userRepresentation.setCredentials(Collections.singletonList(credentials));
            var response = client.realm(REALM).users().create(userRepresentation);
            return response.getStatus() == 201;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean changeUsername(String oldUsername, String newUsername) {
        try {

            UsersResource usersResource = client.realm(REALM).users();
            UserRepresentation user = usersResource.search(oldUsername).get(0);
            user.setUsername(newUsername);
            usersResource.get(user.getId()).update(user);
            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public TokenDTO getToken(String username, String password) {

        try (Keycloak tempClient = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM)
                .username(username)
                .password(password)
                .clientId(CLIENT_ID)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build()) {

            TokenManager tokenmanager = tempClient.tokenManager();

            return new TokenDTO(tokenmanager.getAccessToken().getToken(),tokenmanager.refreshToken().getRefreshToken());

        } catch (Exception e) {
            return null;
        }
    }

    public boolean invalidateToken(String token){
        try {
            client.tokenManager().invalidate(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public TokenDTO doRefreshToken(String refreshToken) {

        String url = SERVER_URL + "/realms/" + REALM + "/protocol/openid-connect/token";
        Configuration kcConfig = new Configuration(SERVER_URL, REALM, CLIENT_ID, null, null);
        Http http = new Http(kcConfig, (params, headers) -> {
        });
        AccessTokenResponse response = http.<AccessTokenResponse>post(url)
                .authentication()
                .client()
                .form()
                .param("grant_type", "refresh_token")
                .param("refresh_token", refreshToken)
                .param("client_id", CLIENT_ID)
                .response()
                .json(AccessTokenResponse.class)
                .execute();

        return new TokenDTO(response.getToken(), response.getRefreshToken());
    }

}
