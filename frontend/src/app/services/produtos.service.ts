import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';
import { BehaviorSubject } from 'rxjs';

export namespace Produto{
  export interface Franquia{
    franquiaSystemId:string,
    franquia:string
  }
  export interface Produto{
    systemId:string,
    franquia:Franquia,
    erpId:number,
    sku:string,
    descricao:string,
    categoria:string,
    unidade:string,
    modelagem:string,
    linha:string,
    colecao:string,
    tipo:string,
    grupo:string,
    subgrupo:string,
    photoUrl:string,
    preco:number
  }
  export interface ProdutoVariacao{
    systemId:string,
    integradorId:string,
    sku:string,
    erpId:number,
    cor:string,
    tamanho:string,
    foto:string
  }
  export interface ProdutoPage{
    content:Produto[],
    totalPages:number,
    totalElements:number,
    last:boolean
  }
  export interface ProdutoEstoque{
    sku:string,
    estoque:VariacaoEstoque[]
  }
  export interface VariacaoEstoque{
    sku:string,
    estoque:number
  }
}
@Injectable({
  providedIn: 'root'
})
export class ProdutosService {

  loadingProdutosSub = new BehaviorSubject<boolean>(false);
  loadingProdutos$ = this.loadingProdutosSub.asObservable();
  produtosSub = new BehaviorSubject<Produto.ProdutoPage | undefined>(undefined);
  produtos$ = this.produtosSub.asObservable();
  pageSub = new BehaviorSubject<number>(0);
  page$ = this.pageSub.asObservable();
  constructor(private http:HttpClient){}

  setProdutosPaged(franquiaId:string){
    this.loadingProdutosSub.next(true);
    this.http.get<Produto.ProdutoPage>(env.BACKEND_URL + `/produtos?page=${this.pageSub.value}&franquiaSystemId=${franquiaId}`).subscribe({
      next:(data)=>{
        this.produtosSub.next(data);
        this.loadingProdutosSub.next(false);
      },
      error:()=>{
        alert("Erro")
      }
    });
  }

  getProductVariation(productId:string){
    return this.http.get<Produto.ProdutoVariacao[]>(env.BACKEND_URL + "/produtos/variacao" + "?productId=" + productId);
  }

  getStock(skus:string[],lojaSlug:string){
    const skusStr = skus.join(",");
    return this.http.get<Produto.ProdutoEstoque[]>(env.BACKEND_URL + "/produtos/estoque" + "?skusBase=" + skusStr + "&lojaSlug="+lojaSlug);
  }

  changePageContext(page:number){
    this.pageSub.next(page);
  }

}
