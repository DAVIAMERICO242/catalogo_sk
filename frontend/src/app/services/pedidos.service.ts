import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Desconto } from './descontos.service';


export namespace Pedidos{
  export interface PedidoCustomerDetails{
    documento:string,
    nome:string,
    numero:number,
    rua:string,
    bairro:string,
    cidade:string,
    estado:string,
    cep:string,
    telefone:string,
  }
  export interface Pedido extends PedidoCustomerDetails{
    systemId:string,
    loja:LojaPedido,
    pago:boolean,
    valorFrete:number
    valor:number,
    produtos:ProdutoPedido[],
    descontosAplicados:DescontoAplicado[]
  }
  export interface LojaPedido{
    systemId:string,
    slug:string,
    nome:string
  }
  export interface ProdutoPedido{
    systemId:string,
    sku:string,
    nome:string,
    valorBase:number,
    variacoesCompradas:VariacaoPedido[],

  }
  export interface VariacaoPedido{
    systemId:string,
    sku:string,
    cor:string,
    tamanho:string,
    valorBase:number,
    fotoUrl:string
  }
  export interface DescontoAplicado{
    systemId:string,
    nome:string,
    tipo:Desconto.DescontoTipo,
    valorAplicado:number
  }
  export namespace PedidoRequestTypes{
    export interface PedidoRequest extends PedidoCustomerDetails{
      loja:LojaPedidoRequest,
      valorFrete:number,
      produtos: ProdutoPedidoRequest[]
    }
    export interface LojaPedidoRequest{
      systemId:string
    }
    export interface ProdutoPedidoRequest{
      systemId:string,
      variacoesCompradas:VariacaoProdutoRequest[]
    }
    export interface VariacaoProdutoRequest{
      systemId:string
    }
  }
}
@Injectable({
  providedIn: 'root'
})
export class PedidosService {

  constructor(private http:HttpClient) {}


}
