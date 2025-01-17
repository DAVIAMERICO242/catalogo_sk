package com.skyler.catalogo.infra.user;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.lojas.Loja;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Value("${app.base-pass}")
    private String basePass;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUsersFranquiaByLoja(Franquia franquia){
        for(Loja loja: franquia.getLojas()){
            if(this.userRepository.findByLoja(loja).isPresent()){
                return;
            }
            User user = new User();
            user.setFranquia(franquia);
            user.setUsername(loja.getSlug());
            user.setPassword(basePass);
            user.setRole(Role.OPERACIONAL);
            user.setLoja(loja);
            user.setBeautyName(loja.getNome());
            this.userRepository.save(user);
        }
    }
}
