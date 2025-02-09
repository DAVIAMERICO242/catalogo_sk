import { Injectable } from '@angular/core';
import { Pedidos } from './pedidos.service';
import { HttpClient } from '@angular/common/http';
import { Desconto, DescontosService } from './descontos.service';
import { env } from '../../env';
import { BehaviorSubject, Subject } from 'rxjs';
import { Loja } from './loja.service';
import { Produto } from './produtos.service';
import { Catalogo } from './catalogo.service';
export namespace Sacola{

  export interface SacolaModel{//não repete produto nem variação
    loja:Loja.Loja,
    produtos:ProdutoCatalogoModel[]
  }

  export interface ProdutoCatalogoModel extends Catalogo.Produto{
    produtoBase:ProdutoBaseModel
  }


  export interface ProdutoBaseModel extends Produto.Produto{
    variacoes:ProdutoVariacaoModel[]
  }

  export interface ProdutoVariacaoModel extends Produto.ProdutoVariacao{
    quantidade:number
  }

  export interface RawSacola{//O MESMO PRODUTO PODE ESTAR VARIAS VEZES NA MESMA SACOLA, CADA UM DELE DEVE TER EXATAMENTE UMA VARIAÇÃO POR MAIS QUE SEJA UM ARRAY
    loja:Pedidos.LojaPedido,
    produtos:Pedidos.ProdutoPedido[]
  }


}
@Injectable({
  providedIn: 'root'
})
export class SacolaService {

  constructor(private http:HttpClient){}

  getDescontoForSacola(raw:Sacola.RawSacola){
    return this.http.post<Desconto.DescontoAplicado[]>(env.BACKEND_URL+"/carrinho/descontos-validos",raw);
  }

  mapModelToRawSacola(model:Sacola.SacolaModel):Sacola.RawSacola{
    const loja:Pedidos.LojaPedido = {
      nome:model.loja.loja,
      slug:model.loja.slug,
      systemId:model.loja.systemId
    }
    let rawProdutos:Pedidos.ProdutoPedido[] = [];
    let rawVariacoes:Pedidos.VariacaoPedido[] = [];
    for(let produtoSacola of model.produtos){
      for(let variacao of produtoSacola.produtoBase.variacoes){
        for(let i=0;i<variacao.quantidade;i++){
          rawVariacoes.push({
            cor:variacao.cor,
            fotoUrl:variacao.foto,
            sku:variacao.sku,
            systemId:variacao.systemId,
            tamanho:variacao.tamanho,
            valorBase:produtoSacola.produtoBase.preco
          })
        }
      }
      const produtoPedido:Pedidos.ProdutoPedido = {
        nome:produtoSacola.produtoBase.descricao,
        sku:produtoSacola.produtoBase.sku,
        valorBase:produtoSacola.produtoBase.preco,
        systemId:produtoSacola.produtoBase.systemId,
        variacoesCompradas:rawVariacoes
      }
      rawProdutos.push(produtoPedido);
    }

    const output:Sacola.RawSacola = {
      loja:loja,
      produtos:rawProdutos
    }

    return output;
  }




}
