import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Produto as ProdutoModel} from "./produtos.service"
import { env } from '../../env';
import { BehaviorSubject, map, tap } from 'rxjs';

export namespace Catalogo{
  export interface CadastroModel{
    systemId:string,
    lojaSlug:string
  }
  export interface Produto{
    systemId:string,
    produtoBase:ProdutoModel.Produto,
    lojaCatalogo:Loja,
    indexOnStore:number
  }
  export interface Loja{
    systemId:string,
    slug:string,
    loja:string
  }
}
@Injectable({
  providedIn: 'root'
})
export class CatalogoService {

  constructor(private http:HttpClient) {}

  contextualCatalogoSub = new BehaviorSubject<Catalogo.Produto[]>([]);
  contextualCatalogo$ = this.contextualCatalogoSub.asObservable();

  reindex(lojaId:string, from:number, to:number){
    return this.http.post<void>(env.BACKEND_URL+`/catalogo/reindex?lojaId=${lojaId}&fromIndex=${from}&toIndex=${to}`,{})
  }

  getCatalogo(slug:string){
    return this.http.get<Catalogo.Produto[]>(env.BACKEND_URL+"/catalogo?lojaSlug="+slug).pipe(
      map((data)=>{
        return data.sort((a,b)=>a.indexOnStore - b.indexOnStore)
      }),
      tap((data)=>{
        this.contextualCatalogoSub.next(data);
      })
    );
  }

  getByProdutoCatalogoId(id:string){
    return this.http.get<Catalogo.Produto>(env.BACKEND_URL+"/catalogo/produto?id="+id);
  }

  adicionarProduto(payload:Catalogo.CadastroModel){
     return this.http.post<Catalogo.Produto>(env.BACKEND_URL+"/catalogo",payload).pipe(
      tap((value)=>{
        const buffer = [...this.contextualCatalogoSub.value];
        buffer.push(value);
        this.contextualCatalogoSub.next(buffer)  
      })
     );
  }

  removerProduto(id:string){
    return this.http.delete<void>(env.BACKEND_URL+"/catalogo?id="+id).pipe(
      tap(()=>{
        this.contextualCatalogoSub.next(this.contextualCatalogoSub.value.filter(e=>e.systemId!==id))
      })
    )
  }


}
