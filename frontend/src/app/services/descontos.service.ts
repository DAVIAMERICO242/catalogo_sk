import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';

export namespace Desconto{
  export interface DescontoModel{
    systemId:string;
    loja:Loja;
    discountName:string;
    descriptionDelimitation?:string,
    isActive:boolean,
    expiresAt:Date,
    cartRequiredQuantity:number,
    totalCartValueDiscount?:number,
    totalCartDecimalPercentDiscount?:number,
    cheapestItemValueDiscount?:number,
    cheapestItemDecimalPercentDiscount?:number,
    expensiveItemValueDiscount?:number,
    expensiveItemDecimalPercentDiscount?:number,
    bonusOutOfCartCatalogProduct?:Produto,
    shippingValueDiscount?:number,
    shippingDecimalPercentDiscount?:number
  }

  export interface Loja{
    nome:string,
    systemId:string,
    slug:string
  }

  export interface Produto{
    nome:string,
    sku:string,
    systemId:string
  }
  export enum DescontoTipo{//so existe no frontend
    DESCONTO_TOTAL_CARRINHO="DESCONTO_TOTAL_CARRINHO",
    DESCONTO_PECA_MAIOR_VALOR="DESCONTO_PECA_MAIOR_VALOR",
    DESCONTO_PECA_MENOR_VALOR="DESCONTO_PECA_MENOR_VALOR",
    DESCONTO_TERMO="DESCONTO_TERMO",
    COMPRE_X_GANHE_1="COMPRE_X_GANHE_1"
  }
}
@Injectable({
  providedIn: 'root'
})
export class DescontosService {

  constructor(private http:HttpClient) {}

  atualizarCadastrarNivelLoja(payload:Desconto.DescontoModel){
    return this.http.post<Desconto.DescontoModel>(env.BACKEND_URL+"/desconto/nivel-loja",payload);
  }

  getAllNivelLoja(lojaSystemId:string){
    return this.http.get<Desconto.DescontoModel[]>(env.BACKEND_URL+"/desconto/nivel-loja");
  }
}
