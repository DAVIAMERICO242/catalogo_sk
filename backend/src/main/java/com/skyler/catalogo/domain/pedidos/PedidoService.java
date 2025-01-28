package com.skyler.catalogo.domain.pedidos;


import com.skyler.catalogo.domain.descontos.carrinho.entities.Desconto;
import com.skyler.catalogo.domain.descontos.carrinho.repositories.DescontoRepository;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import com.skyler.catalogo.domain.produtos.repositories.ProdutoVariacaoRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;
    private final DescontoRepository descontoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoVariacaoRepository produtoVariacaoRepository, DescontoRepository descontoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
        this.descontoRepository = descontoRepository;
    }

    public void novoPedidoUnsecure(PedidoDTO pedidoDTO){
        this.pedidoRepository.save(this.dtoToEntity(pedidoDTO));
    }

    private PedidoDTO entityToDto(Pedido pedido){
        PedidoDTO dto = new PedidoDTO();
        dto.setSystemId(pedido.getSystemId());
        dto.setMoment(pedido.getMoment());
        dto.setDocumento(pedido.getDocumento());
        dto.setNome(pedido.getNome());
        dto.setNumero(pedido.getNumero());
        dto.setRua(pedido.getRua());
        dto.setBairro(pedido.getBairro());
        dto.setCidade(pedido.getCidade());
        dto.setEstado(pedido.getEstado());
        dto.setCep(pedido.getCep());
        dto.setTelefone(pedido.getTelefone());
        dto.setValor(pedido.getValor());
        dto.setValorFrete(pedido.getValorFrete());
        dto.setPago(pedido.getPago());
        for (ProdutoVariacao pv : pedido.getProdutos()) {
            PedidoDTO.ProdutoVariacao pvDto = new PedidoDTO.ProdutoVariacao();
            pvDto.setSystemId(pv.getSystemId());
            pvDto.setSku(pv.getSkuPonto());
            pvDto.setNomeProdutoBase(pv.getProduto().getSystemId());
            pvDto.setCor(pvDto.getCor());
            pvDto.setTamanho(pvDto.getTamanho());
            pvDto.setValorBase(pv.getProduto().getPreco());
            pvDto.setFotoUrl(pv.getFotoUrl());
            dto.addVariacaoComprada(pvDto);
        }
        for(DescontosAplicados descontosAplicados:pedido.getDescontosAplicados()){
            PedidoDTO.Desconto desconto = new PedidoDTO.Desconto();
            desconto.setSystemId(descontosAplicados.getDesconto().getSystemId());
            desconto.setTipo(descontosAplicados.getDesconto().getDescontoTipo());
            desconto.setNome(descontosAplicados.getDesconto().getDiscountName());
            desconto.setValorAplicado(descontosAplicados.getAppliedValue());
            dto.addDescontoAplicado(desconto);
        }

        return dto;
    }


    private Pedido dtoToEntity(PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido();
        if(pedidoDTO.getSystemId()!=null){
            Optional<Pedido> pedidoOptional = this.pedidoRepository.findById(pedidoDTO.getSystemId());
            if(pedidoOptional.isPresent()){
                pedido = pedidoOptional.get();
            }
        }
        pedido.setDocumento(pedidoDTO.getDocumento());
        pedido.setNome(pedidoDTO.getNome());
        pedido.setNumero(pedidoDTO.getNumero());
        pedido.setRua(pedidoDTO.getRua());
        pedido.setBairro(pedidoDTO.getBairro());
        pedido.setCidade(pedidoDTO.getCidade());
        pedido.setEstado(pedidoDTO.getEstado());
        pedido.setCep(pedidoDTO.getCep());
        pedido.setTelefone(pedidoDTO.getTelefone());
        pedido.setValor(pedidoDTO.getValor());
        pedido.setValorFrete(pedidoDTO.getValorFrete());
        List<ProdutoVariacao> variations = this.produtoVariacaoRepository.findAllById(
                pedidoDTO.getVariacoesCompradas().stream().map(o->o.getSystemId()).toList()
        );
        pedido.setProdutos(new HashSet<>(variations));
        if(pedido.getDescontosAplicados().isEmpty()){
            for(PedidoDTO.Desconto desconto:pedidoDTO.getDescontosAplicados()){
                Optional<Desconto> desconto1 = this.descontoRepository.findById(desconto.getSystemId());
                if(desconto1.isPresent()){
                    DescontosAplicados descontosAplicados = new DescontosAplicados();
                    descontosAplicados.setAppliedValue(desconto.getValorAplicado());
                    descontosAplicados.setDesconto(desconto1.get());
                    pedido.addDescontoAplicado(descontosAplicados);
                }
            }
        }
        return pedido;
    }
}
