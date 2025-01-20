package com.skyler.catalogo.domain.produtos.repositories;

import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoVariacaoRepository extends JpaRepository<ProdutoVariacao,String> {


}

