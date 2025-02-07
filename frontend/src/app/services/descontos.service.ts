import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';
import { DatePipe } from '@angular/common';
import { BackendDatePipe } from '../pipes/backend-date.pipe';
import { map } from 'rxjs';

export namespace Desconto{
  export interface DescontoModel{
    systemId:string,
    nome:string,
    tipo:DescontoTipo,
    expiresAt:Date,
    isActive:boolean,
    lojas:LojaModel[],
    createdAt:Date | undefined ,
    descontoFrete?:DescontoFreteModel,
    descontoGenericoCarrinho?:DescontoGenericoCarrinhoModel;
    descontoSimples?:DescontoSimplesProdutoModel,
    descontoSimplesTermo?:DescontoSimplesTermoModel,
    descontoMaiorValor?:DescontoMaiorValorModel,
    descontoMenorValor?:DescontoMenorValorModel,
    descontoProgressivo?:DescontoProgressivoModel
  }
  export interface DescontoFreteModel{
    systemId:string;
    lowerValueLimitToApply:number;
    percentDecimalDiscount:number;
  }
  export interface DescontoSimplesTermoModel extends DelimitedExcludedModel{
    systemId:string,
    percentDecimalDiscount:number
  }
  export interface DescontoSimplesProdutoModel{
    systemId:string,
    produto:ProdutoModel,
    percentDecimalDiscount:number
  }
  export interface ProdutoModel{
    systemId:string,
    nome:string,
    baseValue:number,
  }
  export interface DescontoProgressivoModel extends DelimitedExcludedModel{
    systemId:string,
    intervalos:IntervaloModel[]
  }
  export interface IntervaloModel{
    minQuantity:number,
    percentDecimalDiscount:number
  }
  export interface DescontoMenorValorModel extends DelimitedExcludedModel{
    systemId:string,
    lowerQuantityLimitToApply:number,
    percentDecimalDiscount:number
  }
  export interface DescontoMaiorValorModel extends DelimitedExcludedModel{
    systemId:string,
    lowerQuantityLimitToApply:number,
    percentDecimalDiscount:number
  }
  export interface DescontoGenericoCarrinhoModel{
    systemId:string,
    minValue:number,
    percentDecimalDiscount:number
  }

  interface DelimitedExcludedModel{
    delimitedCategorias:string[],
    excludedCategorias:string[],
    delimitedLinhas:string[],
    excludedLinhas:string[],
    delimitedGrupos:string[],
    excludedGrupos:string[]
  }

  export interface LojaModel{
    systemId:string,
    nome:string,
    slug:string
  }

  export interface DescontoAplicado{
    systemId:string,
    nome:string,
    tipo:DescontoTipo,
    valorAplicado:number
  }

  export enum DescontoTipo{
    DESCONTO_FRETE="DESCONTO_FRETE",
    DESCONTO_GENERICO_CARRINHO="DESCONTO_GENERICO_CARRINHO",
    DESCONTO_SIMPLES_PRODUTO="DESCONTO_SIMPLES_PRODUTO",
    DESCONTO_SIMPLES_TERMO="DESCONTO_SIMPLES_TERMO",
    DESCONTO_PECA_MAIOR_VALOR="DESCONTO_PECA_MAIOR_VALOR",
    DESCONTO_PECA_MENOR_VALOR="DESCONTO_PECA_MENOR_VALOR",
    DESCONTO_PROGRESSIVO="DESCONTO_PROGRESSIVO"
  }

}
@Injectable({
  providedIn: 'root',
})
export class DescontosService {


  constructor(private http:HttpClient,private dateToBackend:BackendDatePipe){
  }

  atualizarCadastrarDesconto(payload:Desconto.DescontoModel){
    const data = {
      ...payload,
      expiresAt:this.dateToBackend.transform(payload.expiresAt)
    }
    return this.http.post<Desconto.DescontoModel>(env.BACKEND_URL+"/descontos",data).pipe(
      map((raw)=>{
        return {
          ...raw,
          expiresAt:new Date(raw.expiresAt + "T00:00:00-03:00"),
          createdAt:new Date((raw.createdAt as Date))
        }
      })
    );
  }

  getDescontosAtivosBySlug(lojaSlug:string){
    let url = env.BACKEND_URL + "/descontos/ativos/by-slug?lojaSlug="+lojaSlug;
    return this.http.get<Desconto.DescontoModel[]>(url).pipe(
      map((raw)=>{
        const buffer = raw.map((e)=>{
          return{
            ...e,
            expiresAt:new Date(e.expiresAt + "T00:00:00-03:00"),
            createdAt:new Date((e.createdAt as Date))
          }
        })
        return buffer;
      })
    );
  }

  getDescontos(franquiaId:string,lojaId?:string){
    let url = env.BACKEND_URL + "/descontos?franquiaId="+franquiaId;
    if(lojaId){
      url = url + "&lojaId="+lojaId
    }
    return this.http.get<Desconto.DescontoModel[]>(url).pipe(
      map((raw)=>{
        const buffer = raw.map((e)=>{
          return{
            ...e,
            expiresAt:new Date(e.expiresAt + "T00:00:00-03:00"),
            createdAt:new Date((e.createdAt as Date))
          }
        })
        return buffer;
      })
    );
  }

  delete(id:string){
    return this.http.delete(env.BACKEND_URL+"/descontos?id="+id);
  }


}
