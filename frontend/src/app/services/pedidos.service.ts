import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Desconto } from './descontos.service';
import { env } from '../../env';
import { map } from 'rxjs';
import { Sacola } from './sacola.service';


export namespace Pedidos{
  export interface PedidoCustomerDetails{
    documento:string,
    nome:string,
    numero:number,
    entregaLoja:boolean,
    rua:string,
    bairro:string,
    cidade:string,
    estado:string,
    cep:string,
    telefone:string,
  }
  export interface Pedido extends PedidoCustomerDetails{
    systemId:string,
    moment:Date,
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
  export namespace PedidoReducedTypes{
    export interface PedidoReduced extends Pedido{
      produtos:ProdutosPedidoReduced[]
    }
    export interface ProdutosPedidoReduced extends ProdutoPedido{
      variacoesCompradas:VariacaoPedidoReduced[]
    }

    export interface VariacaoPedidoReduced extends VariacaoPedido{
      quantidade:number
    }
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

  mapRawSacolaAndCustomerAndValorFreteToPedidoRequest(
    rawSacola:Sacola.RawSacola,
    customerDetails:Pedidos.PedidoCustomerDetails,
    frete:number

  ):Pedidos.PedidoRequestTypes.PedidoRequest{
    return {
      loja:rawSacola.loja,
      produtos:rawSacola.produtos,
      valorFrete:frete,
      ...customerDetails
    }
  }

  novoPedido(payload:Pedidos.PedidoRequestTypes.PedidoRequest){//eu te oodeio, ME DEIXA EM PAZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ, ME DEIXA EM PAZ
    return this.http.post(env.BACKEND_URL+"/pedidos",payload);
  }

  deletarPedido(id:string){
    return this.http.delete(env.BACKEND_URL+"/pedidos?id="+id);
  }

  getPedidos(franquiaId:string,lojaId?:string){
    let url = env.BACKEND_URL+"/pedidos?franquiaId="+franquiaId
    if(lojaId){
      url = url + "&lojaId="+lojaId
    }
    return this.http.get<Pedidos.Pedido[]>(url).pipe(
      map((data)=>{
        return data.map((e)=>{
          return{
            ...e,
            moment: new Date(e.moment)
          }
        })
      })
    )
  }

  alterarStatus(pedidoId:string){
    return this.http.put(env.BACKEND_URL+"/pedidos/mudar-status?pedidoId="+pedidoId,{})
  }

  reducePedido(pedido:Pedidos.Pedido):Pedidos.PedidoReducedTypes.PedidoReduced{
    const buffer:Pedidos.PedidoReducedTypes.PedidoReduced = {
      ...pedido,
      produtos:pedido.produtos.map((e)=>{
         return{
          ...e,
          variacoesCompradas: [
            ...e.variacoesCompradas.reduce((map, e1) => {
              if (!map.has(e1.systemId)) {
                map.set(e1.systemId, { ...e1, quantidade: 0 });
              }
              map.get(e1.systemId)!.quantidade += 1;
              return map;
            }, new Map<string, Pedidos.PedidoReducedTypes.VariacaoPedidoReduced>()).values()
          ]
         }
      })
    }
    return buffer;
  }


}
