package com.skyler.catalogo.infra.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse
 {
    String beautyName;
    String username;
    String password;
    String token;
    Boolean shouldChangeFirstPass;
    Loja loja;
    Franquia franquia;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Loja{
        String nome;
        String slug;
        String systemId;
    }

     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     public static class Franquia{
         String nome;
         String systemId;
     }
}
