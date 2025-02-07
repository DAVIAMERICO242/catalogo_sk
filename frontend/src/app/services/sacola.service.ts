import { Injectable } from '@angular/core';
import { Pedidos } from './pedidos.service';
import { HttpClient } from '@angular/common/http';
import { Desconto } from './descontos.service';
import { env } from '../../env';
import { BehaviorSubject, Subject } from 'rxjs';
export namespace Sacola{

  export interface RawSacola{//O MESMO PRODUTO PODE ESTAR VARIAS VEZES NA MESMA SACOLA, CADA UM DELE DEVE TER EXATAMENTE UMA VARIAÇÃO POR MAIS QUE SEJA UM ARRAY
    loja:Pedidos.LojaPedido,
    produtos:Pedidos.ProdutoPedido[]
  }

  export interface ProdutoSacolaRequest{
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

  private onSacolaChangeSub = new Subject();
  onSacolaChange$ = this.onSacolaChangeSub.asObservable();
  private openSub = new BehaviorSubject<boolean>(false);
  open$= this.openSub.asObservable();

  private readonly sacolasStorageName = "sacolas-lojas";

  constructor(private http:HttpClient){}

  notifySacolaChange(){
    this.onSacolaChangeSub.next(1);
  }

  openSacola(){
    this.openSub.next(true);
  }

  closeSacola(){
    this.openSub.next(false);
  }

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
        const nome = raw?.produtos.find((e)=>e.variacoesCompradas.map(e=>e.systemId).includes(item.systemId))?.nome as string;
        mapa.set(item.systemId, { ...item,nome: nome,quantidade: 1 });
      }
    });
    return {
      itens:Array.from(mapa.values())
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

  addToSacolaForLoja(loja:Pedidos.LojaPedido,produto:Sacola.ProdutoSacolaRequest):void{
    const sacolasString = localStorage.getItem(this.sacolasStorageName);
    if(sacolasString){
      let sacolas = JSON.parse(sacolasString) as Sacola.RawSacola[];
      let sacolaForLoja = sacolas.find(e=>e.loja.systemId===loja.systemId);
      if(sacolaForLoja){
        const foundProduto = sacolaForLoja.produtos.find((e)=>e.systemId===produto.systemId);
        if(foundProduto){
          foundProduto.variacoesCompradas.unshift(produto.variacaoAlvo);
          sacolaForLoja.produtos = sacolaForLoja.produtos.map((e)=>{
            if(e.systemId===produto.systemId){
              return foundProduto;
            }else{
              return e;
            }
          })
        }else{
          const produtoSacola:Pedidos.ProdutoPedido = {
            nome:produto.nome,
            sku:produto.sku,
            systemId:produto.systemId,
            valorBase:produto.valorBase,
            variacoesCompradas:[produto.variacaoAlvo]
          }
          sacolaForLoja.produtos = [...sacolaForLoja.produtos,produtoSacola];
        }
        sacolas = sacolas.map((e)=>{
          if(e.loja.systemId===loja.systemId){
            return sacolaForLoja;
          }else{
            return e;
          }
        });
        localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
      }else{
        const produtoSacola:Pedidos.ProdutoPedido = {
          nome:produto.nome,
          sku:produto.sku,
          systemId:produto.systemId,
          valorBase:produto.valorBase,
          variacoesCompradas:[produto.variacaoAlvo]
        }
        sacolas.unshift({
          loja,
          produtos:[produtoSacola]
        });
        localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
      }
    }else{
      const produtoSacola:Pedidos.ProdutoPedido = {
        nome:produto.nome,
        sku:produto.sku,
        systemId:produto.systemId,
        valorBase:produto.valorBase,
        variacoesCompradas:[produto.variacaoAlvo]
      }
      const sacolas:Sacola.RawSacola[] = [{
        loja:loja,
        produtos:[produtoSacola]
      }];
      localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
    }
    this.notifySacolaChange();
  }

  removeExactlyOneQuantityOfItemFromSacolaLoja(loja:Pedidos.LojaPedido,variationSystemId:string){//id do produto base
    const sacolasString = localStorage.getItem(this.sacolasStorageName);
    if(sacolasString){
      let sacolas = JSON.parse(sacolasString) as Sacola.RawSacola[];
      let sacolaForLoja = sacolas.find(e=>e.loja.systemId===loja.systemId);
      if(sacolaForLoja){
        let foundProduto = sacolaForLoja.produtos.find((e)=>e.variacoesCompradas.map((e)=>e.systemId).includes(variationSystemId));
        if(!foundProduto){
          throw new Error("Estado inválido");
        }
        if(foundProduto.variacoesCompradas.length===1){
          sacolaForLoja.produtos = [...sacolaForLoja.produtos.filter((e)=>!e.variacoesCompradas.map((e)=>e.systemId).includes(variationSystemId))];
        }else if(foundProduto.variacoesCompradas.length>1){
          const index = foundProduto.variacoesCompradas.findIndex(e => e.systemId === variationSystemId);
          if (index !== -1) {
            foundProduto.variacoesCompradas.splice(index, 1); // Remove exatamente uma unidade da variação
          }
          sacolaForLoja.produtos = sacolaForLoja.produtos.map((e)=>{
            if(e.systemId===foundProduto.systemId){
              return foundProduto;
            }else{
              return e;
            }
          });
        }else{
          throw new Error("Estado inválido");
        }
        sacolas = sacolas.map((e)=>{
          if(e.loja.systemId===loja.systemId){
            return sacolaForLoja;
          }else{
            return e;
          }
        });
        localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
      }else{
        throw new Error("Estado inválido");
      }
    }else{
      throw new Error("Estado inválido");
    }
    this.notifySacolaChange();
  }


}
