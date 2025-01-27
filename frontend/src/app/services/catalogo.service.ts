import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Produto as ProdutoModel} from "./produtos.service"
import { env } from '../../env';
import { BehaviorSubject, tap } from 'rxjs';

export namespace Catalogo{
  export interface CadastroModel{
    systemId:string,
    lojaSlug:string
  }
  export interface DeletarModel{
    systemId:string,
    lojaSlug:string
  }
  export interface Produto{
    systemId:string,
    produtoBase:ProdutoModel.Produto,
    lojaCatalogo:Loja,
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

  getCatalogo(slug:string){
    return this.http.get<Catalogo.Produto[]>(env.BACKEND_URL+"/catalogo?lojaSlug="+slug).pipe(
      tap((data)=>{
        this.contextualCatalogoSub.next(data);
      })
    );
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

  removerProduto(payload:Catalogo.DeletarModel){
    return this.http.delete<void>(env.BACKEND_URL+"/catalogo",{body:payload}).pipe(
      tap(()=>{
        this.contextualCatalogoSub.next(this.contextualCatalogoSub.value.filter(e=>e.produtoBase.systemId!==payload.systemId && e.lojaCatalogo.slug!==payload.lojaSlug))
      })
    )
  }


}
