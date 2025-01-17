package com.skyler.catalogo.infra.user;

import com.skyler.catalogo.domain.lojas.Loja;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query("SELECT u FROM User u " +
            "WHERE u.loja.systemId = :lojaId AND u.password = :password ")
    Optional<User> findByLojaIdAndPassword(String lojaId, String password);

    @Query("SELECT u FROM User u " +
            "WHERE u.loja = :loja ")
    Optional<User> findByLoja(Loja loja);
}
