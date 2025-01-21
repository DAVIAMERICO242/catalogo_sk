import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Produto as ProdutoModel} from "./produtos.service"
import { env } from '../../env';

export namespace Catalogo{
  export interface CadastroModel{
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

  adicionarProduto(payload:Catalogo.CadastroModel){
     return this.http.post<Catalogo.Produto>(env.BACKEND_URL+"/catalogo",{payload});
  }
}
