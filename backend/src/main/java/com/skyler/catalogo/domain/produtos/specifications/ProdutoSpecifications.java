package com.skyler.catalogo.domain.produtos.specifications;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import org.springframework.data.jpa.domain.Specification;

public class ProdutoSpecifications {
    public static Specification<Produto> hasFranquiaWithoutVariacoes(Franquia franquia) {
        return (root, query, criteriaBuilder) -> {
            Class clazz = query.getResultType();
            if (clazz.equals(Long.class) || clazz.equals(long.class))
                return null;
            // Força o JOIN FETCH apenas na franquia
            root.fetch("franquia");

            // Evita duplicação nos resultados da paginação
            query.distinct(true);

            // Adiciona o critério de igualdade para a franquia
            return criteriaBuilder.equal(root.get("franquia"), franquia);
        };
    }

    public static Specification<Produto> nomeContains(String nome){
        if(nome==null || nome.isBlank()){
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> {
            Class clazz = query.getResultType();
            if (clazz.equals(Long.class) || clazz.equals(long.class))
                return null;
            return criteriaBuilder.like(root.get("descricao"),"%"+nome+"%");
        };
    }

    public static Specification<Produto> skuContains(String sku){
        if(sku==null || sku.isBlank()){
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> {
            Class clazz = query.getResultType();
            if (clazz.equals(Long.class) || clazz.equals(long.class))
                return null;
            return criteriaBuilder.like(root.get("sku"),"%"+sku+"%");
        };
    }
}
