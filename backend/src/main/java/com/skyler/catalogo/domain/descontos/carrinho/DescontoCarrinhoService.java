package com.skyler.catalogo.domain.descontos.carrinho;

import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogoRepository;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DescontoCarrinhoService {

    private final LojaRepository lojaRepository;
    private final ProdutoCatalogoRepository produtoCatalogoRepository;
    private final DescontoCarrinhoRepository descontoCarrinhoRepository;

    public DescontoCarrinhoService(LojaRepository lojaRepository, ProdutoCatalogoRepository produtoCatalogoRepository, DescontoCarrinhoRepository descontoCarrinhoRepository) {
        this.lojaRepository = lojaRepository;
        this.produtoCatalogoRepository = produtoCatalogoRepository;
        this.descontoCarrinhoRepository = descontoCarrinhoRepository;
    }


    public DescontoCarrinhoDTO criarAtualizarDesconto(DescontoCarrinhoDTO payload){
        DescontoCarrinhoDTO output = new DescontoCarrinhoDTO();
        DescontoCarrinho entity = this.dtoToEntity(payload);
        this.descontoCarrinhoRepository.save(entity);
        return output;
    }

    private DescontoCarrinho dtoToEntity(DescontoCarrinhoDTO dto){
        DescontoCarrinho entity = new DescontoCarrinho();
        if(dto.getSystemId()!=null && !dto.getSystemId().isBlank()){
            Optional<DescontoCarrinho> opt = this.descontoCarrinhoRepository.findById(dto.getSystemId());
            if(opt.isEmpty()){
                throw new RuntimeException("Id do desconto inválido");
            }
            entity = opt.get();
        }
        if(entity.getSystemId()!=null && !entity.getSystemId().isBlank()){
            entity.setSystemId(dto.getSystemId());
        }
        Optional<Loja> lojaOPT = this.lojaRepository.findById(dto.getLoja().getSystemId());
        if(lojaOPT.isEmpty()){
            throw new RuntimeException("Loja não encontrada");
        }
        entity.setLoja(lojaOPT.get());
        entity.setDiscountName(dto.getDiscountName());
        entity.setDescriptionDelimitation(dto.getDescriptionDelimitation());
        entity.setIsActive(dto.getIsActive());
        entity.setExpiresAt(dto.getExpiresAt());
        entity.setCartRequiredQuantity(dto.getCartRequiredQuantity());
        entity.setTotalCartValueDiscount(dto.getTotalCartValueDiscount());
        entity.setTotalCartDecimalPercentDiscount(dto.getTotalCartDecimalPercentDiscount());
        entity.setCheapestItemValueDiscount(dto.getCheapestItemValueDiscount());
        entity.setCheapestItemDecimalPercentDiscount(dto.getCheapestItemDecimalPercentDiscount());
        entity.setExpensiveItemValueDiscount(dto.getExpensiveItemValueDiscount());
        entity.setExpensiveItemDecimalPercentDiscount(dto.getExpensiveItemDecimalPercentDiscount());
        String productId = Optional.ofNullable(dto.getBonusOutOfCartCatalogProduct()).map(o->o.getSystemId()).orElse(null);
        if(productId!=null && !productId.isBlank()){
            Optional<ProdutoCatalogo> produtoOPT = this.produtoCatalogoRepository.findById(productId);
            if(produtoOPT.isEmpty()){
                throw new RuntimeException("Produto não encontrado");
            }
            entity.setBonusOutOfCartCatalogProduct(produtoOPT.get());
        }
        entity.setShippingValueDiscount(dto.getShippingValueDiscount());
        entity.setShippingDecimalPercentDiscount(dto.getShippingDecimalPercentDiscount());

        return entity;
    }
}
