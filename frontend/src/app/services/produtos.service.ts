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
    preco:number,
    produtoCatalogoId?:string,
    variacoes:ProdutoVariacao[]
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
  export interface Filter{
    nome?:string,
    sku?:string
  }
  export interface Termos{
    categorias:string[],
    linhas:string[],
    grupos:string[]
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
  filterSub = new BehaviorSubject<Produto.Filter>({});
  filter$ = this.filterSub.asObservable();

  constructor(private http:HttpClient){}

  setProdutosPaged(lojaSlug: string, nome?: string, sku?: string) {
    const params = new URLSearchParams();
  
    if (nome) {
      params.append("nome", nome);
    }
    if (sku) {
      params.append("sku", sku);
    }
  
    this.loadingProdutosSub.next(true);
  
    const queryParams = params.toString();
    const url = env.BACKEND_URL + `/produtos?page=${this.pageSub.value}&lojaSlug=${lojaSlug}` 
                + (queryParams ? `&${queryParams}` : "");
  
    this.http.get<Produto.ProdutoPage>(url).subscribe({
      next: (data) => {
        this.changeFilter({nome,sku})
        this.produtosSub.next(data);
        this.loadingProdutosSub.next(false);
      },
      error: () => {
        alert("Erro");
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

  getVariationStock(sku:string,lojaSlug:string){
    return this.http.get<Produto.VariacaoEstoque>(env.BACKEND_URL+"/produtos/estoque-variacao?sku="+sku +"&lojaSlug="+lojaSlug)
  }

  getTermos(franquiaId:string){
    return this.http.get<Produto.Termos>(env.BACKEND_URL+"/produtos/termos?franquiaSystemId="+franquiaId)
  }

  changePageContext(page:number){
    this.pageSub.next(page);
  }

  changeFilter(filter:Produto.Filter){
    this.filterSub.next(filter);
  }

}
