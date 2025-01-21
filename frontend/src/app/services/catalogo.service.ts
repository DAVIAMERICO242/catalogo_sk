import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Produto as ProdutoModel} from "./produtos.service"
import { env } from '../../env';

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
    produtoBase:ProdutoModel.Produto,
    loja:Loja,
    valorCatalogo:number
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

  getCatalogo(slug:string){
    return this.http.get<Catalogo.Produto[]>(env.BACKEND_URL+"/catalogo?lojaSlug="+slug);
  }

  adicionarProduto(payload:Catalogo.CadastroModel){
     return this.http.post<Catalogo.Produto>(env.BACKEND_URL+"/catalogo",payload);
  }

  removerProduto(payload:Catalogo.DeletarModel){
    return this.http.delete<void>(env.BACKEND_URL+"/catalogo",{body:payload})
  }


}
