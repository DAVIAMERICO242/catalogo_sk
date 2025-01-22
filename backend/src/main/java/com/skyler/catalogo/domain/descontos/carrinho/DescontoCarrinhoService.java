package com.skyler.catalogo.domain.descontos.carrinho;

import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogoRepository;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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


    public List<DescontoCarrinhoDTO> getDescontos(String lojaSystemId){
        List<DescontoCarrinho> entities = this.descontoCarrinhoRepository.findAllByLojaId(lojaSystemId);
        List<DescontoCarrinhoDTO> output = new ArrayList<>();
        for(DescontoCarrinho entity:entities){
            output.add(this.entityToDto(entity));
        }
        return output;
    }


    public DescontoCarrinhoDTO criarAtualizarDesconto(DescontoCarrinhoDTO payload){
        DescontoCarrinhoDTO output = new DescontoCarrinhoDTO();
        DescontoCarrinho entity = this.dtoToEntity(payload);
        this.descontoCarrinhoRepository.save(entity);
        return output;
    }

    private DescontoCarrinhoDTO entityToDto(DescontoCarrinho entity){
        DescontoCarrinhoDTO dto = new DescontoCarrinhoDTO();

        if (entity.getSystemId() != null && !entity.getSystemId().isBlank()) {
            dto.setSystemId(entity.getSystemId());
        }

        if (entity.getLoja() != null) {
            DescontoCarrinhoDTO.Loja lojaDTO = new DescontoCarrinhoDTO.Loja();
            lojaDTO.setSystemId(entity.getLoja().getSystemId());
            lojaDTO.setNome(entity.getLoja().getNome());
            lojaDTO.setSlug(entity.getLoja().getSlug());
            dto.setLoja(lojaDTO);
        }

        dto.setDiscountName(entity.getDiscountName());
        dto.setDescriptionDelimitation(entity.getDescriptionDelimitation());
        dto.setIsActive(entity.getIsActive());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setCartRequiredQuantity(entity.getCartRequiredQuantity());
        dto.setTotalCartValueDiscount(entity.getTotalCartValueDiscount());
        dto.setTotalCartDecimalPercentDiscount(entity.getTotalCartDecimalPercentDiscount());
        dto.setCheapestItemValueDiscount(entity.getCheapestItemValueDiscount());
        dto.setCheapestItemDecimalPercentDiscount(entity.getCheapestItemDecimalPercentDiscount());
        dto.setExpensiveItemValueDiscount(entity.getExpensiveItemValueDiscount());
        dto.setExpensiveItemDecimalPercentDiscount(entity.getExpensiveItemDecimalPercentDiscount());

        if (entity.getBonusOutOfCartCatalogProduct() != null) {
            DescontoCarrinhoDTO.Produto produtoDTO = new DescontoCarrinhoDTO.Produto();
            produtoDTO.setSystemId(entity.getBonusOutOfCartCatalogProduct().getSystemId());
            produtoDTO.setSku(entity.getBonusOutOfCartCatalogProduct().getProdutoBaseFranquia().getSku());
            produtoDTO.setNome(entity.getBonusOutOfCartCatalogProduct().getProdutoBaseFranquia().getDescricao());
            // Preencha outros campos do ProdutoCatalogoDTO, se necessário
            dto.setBonusOutOfCartCatalogProduct(produtoDTO);
        }

        dto.setShippingValueDiscount(entity.getShippingValueDiscount());
        dto.setShippingDecimalPercentDiscount(entity.getShippingDecimalPercentDiscount());

        return dto;
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
