import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { Pedidos } from '../../services/pedidos.service';
import { Sacola, SacolaService } from '../../services/sacola.service';
import { BehaviorSubject, Subject } from 'rxjs';
import { Desconto } from '../../services/descontos.service';
import { Loja } from '../../services/loja.service';
import { Catalogo } from '../../services/catalogo.service';
import { Produto } from '../../services/produtos.service';
import { SacolaComponent } from './sacola.component';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class SacolaUiContextService {
  private readonly sacolasStorageName = "sacolas-lojas";
    private onSacolaChangeSub = new Subject();
    onSacolaChange$ = this.onSacolaChangeSub.asObservable();
    private openSub = new BehaviorSubject<boolean>(false);
    open$= this.openSub.asObservable();
    private descontosAplicadosSub = new BehaviorSubject<Desconto.DescontoAplicado[]>([]);
    descontosAplicados$ = this.descontosAplicadosSub.asObservable();
    loadingDescontosSub = new BehaviorSubject<boolean>(false);

  constructor(private sacolaService:SacolaService,@Inject(PLATFORM_ID) private platformId: Object) { }

  openSacola(){
    this.openSub.next(true);
  }

  closeSacola(){
    this.openSub.next(false);
  }


  setDescontos(sacola:Sacola.SacolaModel){
    const raw = this.sacolaService.mapModelToRawSacola(sacola);
    this.loadingDescontosSub.next(true);
    this.sacolaService.getDescontoForSacola(raw).subscribe((data)=>{
      this.loadingDescontosSub.next(false);
      this.descontosAplicadosSub.next(data);
    })
  }

  notifySacolaChange(){
    this.onSacolaChangeSub.next(1);
  }


  getSacolaForLoja(loja:Loja.Loja):Sacola.SacolaModel | undefined{
    if (isPlatformBrowser(this.platformId)) {
      const sacolasString = localStorage.getItem(this.sacolasStorageName);
      if(sacolasString){
        let sacolas = JSON.parse(sacolasString) as Sacola.SacolaModel[];
        return sacolas.find((e)=>e.loja.systemId===loja.systemId)
      }
    }
    return undefined;
  }


  limparSacola(loja:Loja.Loja){
    if (isPlatformBrowser(this.platformId)) {
      const sacolasString = localStorage.getItem(this.sacolasStorageName);
      if(sacolasString){
        let sacolas = JSON.parse(sacolasString) as Sacola.SacolaModel[];
        let sacolaForLoja = sacolas.find(e=>e.loja.systemId===loja.systemId);
        if(sacolaForLoja){
          sacolas = sacolas.filter((e)=>e.loja.systemId!==loja.systemId);
        }
        localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
      }
      this.descontosAplicadosSub.next([]);
      this.notifySacolaChange();
    }
  }

  addToSacolaForLoja(loja:Loja.Loja,produto:Catalogo.Produto,variacao:Produto.ProdutoVariacao):void{
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    const sacolasString = localStorage.getItem(this.sacolasStorageName);
    if(sacolasString){
      let sacolas = JSON.parse(sacolasString) as Sacola.SacolaModel[];
      let sacolaForLoja = sacolas.find(e=>e.loja.systemId===loja.systemId);
      if(sacolaForLoja){
        let foundProduto = sacolaForLoja.produtos.find((e)=>e.systemId===produto.systemId);
        if(foundProduto){
          const foundVariacao = foundProduto.produtoBase.variacoes.find((e)=>e.systemId===variacao.systemId);
          if(foundVariacao){
            sacolaForLoja.produtos = sacolaForLoja.produtos.map((e)=>{
              if(e.systemId===produto.systemId){
                const produtoBase:Sacola.ProdutoBaseModel = {
                  ...e.produtoBase,
                  variacoes:[...e.produtoBase.variacoes.map((v)=>{
                    if(v.systemId===variacao.systemId){
                      return{
                        ...v,
                        quantidade:v.quantidade + 1
                      }
                    }else{
                      return v
                    }
                  })]
                }
                return {
                  ...foundProduto,
                  produtoBase
                };
              }else{
                return e;
              }
            })
          }else{
            foundProduto.produtoBase.variacoes.unshift({
              ...variacao,
              quantidade:1
            });
            sacolaForLoja.produtos = sacolaForLoja.produtos.map((e)=>{
              if(e.systemId===foundProduto.systemId){
                return foundProduto;
              }else{
                return e;
              }
            })
          }
        }else{
          const produtoBase:Sacola.ProdutoBaseModel = {
            ...produto.produtoBase,
            variacoes:[{
              ...variacao,
              quantidade:1
            }]
          }
          const produtoSacola:Sacola.ProdutoCatalogoModel = {
            lojaCatalogo:loja,
            systemId:produto.systemId,
            produtoBase:produtoBase,
            indexOnStore:produto.indexOnStore
          }
          sacolaForLoja.produtos.unshift(produtoSacola);
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
        const produtoBase:Sacola.ProdutoBaseModel = {
          ...produto.produtoBase,
          variacoes:[{
            ...variacao,
            quantidade:1
          }]
        }
        const sacolaForLoja:Sacola.SacolaModel = {
          loja:loja,
          produtos:[{
            ...produto,
            produtoBase
          }]
        }
        sacolas.unshift(sacolaForLoja);
        localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
      }
    }else{
      const produtoBase:Sacola.ProdutoBaseModel = {
        ...produto.produtoBase,
        variacoes:[{
          ...variacao,
          quantidade:1
        }]
      }
      const sacolaForLoja:Sacola.SacolaModel = {
        loja:loja,
        produtos:[{
          ...produto,
          produtoBase
        }]
      }
      const sacolas:Sacola.SacolaModel[] = [sacolaForLoja];
      localStorage.setItem(this.sacolasStorageName,JSON.stringify(sacolas));
    }
    this.notifySacolaChange();
  }

  removeExactlyOneQuantityOfItemFromSacolaLoja(loja:Loja.Loja,variationSystemId:string){//id do produto base
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    const sacolasString = localStorage.getItem(this.sacolasStorageName);
    if(sacolasString){
      let sacolas = JSON.parse(sacolasString) as Sacola.SacolaModel[];
      let sacolaForLoja = sacolas.find(e=>e.loja.systemId===loja.systemId);
      if(sacolaForLoja){
        let foundProduto = sacolaForLoja.produtos.find((e)=>e.produtoBase.variacoes.map((e)=>e.systemId).includes(variationSystemId)) as Sacola.ProdutoCatalogoModel;
        const quantidadeVariacoes = foundProduto.produtoBase.variacoes.reduce((a,b)=>a+b.quantidade,0);
        if(quantidadeVariacoes===1){
          sacolaForLoja.produtos = [...sacolaForLoja.produtos.filter((e)=>!e.produtoBase.variacoes.map((e)=>e.systemId).includes(variationSystemId))];
        }else if(quantidadeVariacoes>1){
          const regardingVariacao = foundProduto.produtoBase.variacoes.find((e)=>e.systemId===variationSystemId);
          if(regardingVariacao?.quantidade===1){
            foundProduto.produtoBase.variacoes = foundProduto.produtoBase.variacoes.filter((e)=>e.systemId!==variationSystemId);
          }else if(regardingVariacao && regardingVariacao?.quantidade>1){
            foundProduto = {
              ...foundProduto,
              produtoBase:{
                ...foundProduto.produtoBase,
                variacoes: foundProduto.produtoBase.variacoes.map((e)=>{
                  if(e.systemId===variationSystemId){
                    return{
                      ...e,
                      quantidade: e.quantidade - 1
                    }
                  }else{
                    return e
                  }
                })
              }
            }
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
