package com.skyler.catalogo.infra.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    List<Loja> lojasFranquia = new ArrayList<>();
    Role role;

    public void addLojaFranquia(Loja loja){
        this.lojasFranquia.add(loja);
    }

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
         Boolean isMatriz;
     }
}
