import { Injectable } from '@angular/core';
import { Pedidos } from './pedidos.service';
import { HttpClient } from '@angular/common/http';
import { Desconto } from './descontos.service';
import { env } from '../../env';
export namespace Sacola{

  export interface RawSacola{
    loja:Pedidos.LojaPedido,
    produtos:Pedidos.ProdutoPedido[]
  }

  export interface BeautySacola {
    itens:BeautySacolaItem[]
  }

  export interface BeautySacolaItem extends Pedidos.VariacaoPedido{
    quantidade:number;
  }


}
@Injectable({
  providedIn: 'root'
})
export class SacolaService {

  private readonly sacolasStorageName = "sacolas-lojas";

  constructor(private http:HttpClient){}

  getDescontoForSacola(raw:Sacola.RawSacola){
    return this.http.post<Desconto.DescontoAplicado[]>(env.BACKEND_URL+"/carrinho/descontos-validos",raw);
  }

  getBeautySacolaForLoja(loja:Pedidos.LojaPedido):Sacola.BeautySacola | undefined{
    const raw = this.getRawSacolaForLoja(loja);
    const variacoes = raw?.produtos.flatMap((e)=>e.variacoesCompradas);
    const mapa = new Map<string, Sacola.BeautySacolaItem>();
    variacoes?.forEach((item) => {
      if (mapa.has(item.systemId)) {
        mapa.get(item.systemId)!.quantidade += 1;
      } else {
        mapa.set(item.systemId, { ...item, quantidade: 1 });
      }
    });
    return {
      itens:Array.from(mapa.values());
    }
  }

  getRawSacolaForLoja(loja:Pedidos.LojaPedido):Sacola.RawSacola | undefined{
    const sacolasString = localStorage.getItem(this.sacolasStorageName);
    if(sacolasString){
      let sacolas = JSON.parse(sacolasString) as Sacola.RawSacola[];
      let sacolaForLoja = sacolas.find(e=>e.loja.systemId===loja.systemId);
      return sacolaForLoja;
    }
    return undefined;
  }

  addToSacolaForLoja(loja:Pedidos.LojaPedido,produto:Pedidos.ProdutoPedido):void{
    const sacolasString = localStorage.getItem(this.sacolasStorageName);
    if(sacolasString){
      let sacolas = JSON.parse(sacolasString) as Sacola.RawSacola[];
      let sacolaForLoja = sacolas.find(e=>e.loja.systemId===loja.systemId);
      if(sacolaForLoja){
        sacolaForLoja.produtos = [...sacolaForLoja.produtos,produto];
        sacolas = sacolas.map((e)=>{
          if(e.loja.systemId===loja.systemId){
            return sacolaForLoja;
          }else{
            return e;
          }
        });
        localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
      }else{
        sacolas.push({
          loja,
          produtos:[produto]
        });
        localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
      }
    }else{
      const sacolas:Sacola.RawSacola[] = [{
        loja:loja,
        produtos:[produto]
      }];
      localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
    }
  }

  removeExactlyOneQuantityOfItemFromSacolaLoja(loja:Pedidos.LojaPedido,productSystemId:string){//id do produto base
    const sacolasString = localStorage.getItem(this.sacolasStorageName);
    if(sacolasString){
      let sacolas = JSON.parse(sacolasString) as Sacola.RawSacola[];
      let sacolaForLoja = sacolas.find(e=>e.loja.systemId===loja.systemId);
      if(sacolaForLoja){
        const foundIndex = sacolaForLoja.produtos.findIndex(e=>e.systemId===productSystemId);
        sacolaForLoja.produtos.splice(foundIndex,1);
        sacolas = sacolas.map((e)=>{
          if(e.loja.systemId===loja.systemId){
            return sacolaForLoja;
          }else{
            return e;
          }
        });
      }else{
        throw new Error("Estado inválido");
      }
    }else{
      throw new Error("Estado inválido");
    }
  }


}
