import { Injectable } from '@angular/core';
import { Pedidos } from './pedidos.service';
import { HttpClient } from '@angular/common/http';
import { Desconto, DescontosService } from './descontos.service';
import { env } from '../../env';
import { BehaviorSubject, Subject } from 'rxjs';
import { ProdutoPrecificavel } from '../loja/produto-preco/produto-preco.component';
export namespace Sacola{

  export interface RawSacola{//O MESMO PRODUTO PODE ESTAR VARIAS VEZES NA MESMA SACOLA, CADA UM DELE DEVE TER EXATAMENTE UMA VARIAÇÃO POR MAIS QUE SEJA UM ARRAY
    loja:Pedidos.LojaPedido,
    produtos:ProdutoRawSacola[]
  }

  export interface ProdutoRawSacola extends Pedidos.ProdutoPedido, ProdutoPrecificavel{  
  }

  export interface ProdutoSacolaRequest extends ProdutoPrecificavel{
    systemId:string,
    sku:string,
    nome:string,
    valorBase:number,
    variacaoAlvo:Pedidos.VariacaoPedido
  }

  export interface BeautySacola {
    itens:BeautySacolaItem[]
  }

  export interface BeautySacolaItem extends Pedidos.VariacaoPedido{
    nome:string;
    quantidade:number;
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




}
