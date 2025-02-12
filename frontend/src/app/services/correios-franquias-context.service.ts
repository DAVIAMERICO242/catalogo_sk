import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';


export namespace CorreiosFranquiasContext{
  export interface CorreiosFranquias{
    systemId:string,
    franquiaId:string,
    usuario:string,
    senha:string,
    codigoPac:string,
    codigoSedex:string,
    cepOrigem:string,
    numeroDiretoriaRegional:string,
    numeroContrato:string,
    numeroCartaoPostal:string
  }
  export interface PesoCategoria{
    systemId:string,
    franquiaId:string,
    categoria:string,
    pesoGramas:number
  }
  export interface DimensaoCaixa{
    systemId:string,
    franquiaId:string,
    largura:number,
    comprimento:number,
    altura:number
  }

  export interface FaixaCep{
    systemId:string,
    nome:string,
    franquiaId:string,
    cepInicio:string,
    cepFim:string,
    minValueToApply:number,
    valorFixo:number,
    prazo:number
  }
}
@Injectable({
  providedIn: 'root'
})
export class CorreiosFranquiasContextService {

  constructor(private http:HttpClient) { }

  criarAtualizarIntegracaoConfig(payload:CorreiosFranquiasContext.CorreiosFranquias){
    return this.http.post<CorreiosFranquiasContext.CorreiosFranquias>(env.BACKEND_URL+"/correio-franquia-context",payload);
  }

  getIntegracaoConfigByFranquiaId(franquiaId:string){
    return this.http.get<CorreiosFranquiasContext.CorreiosFranquias | undefined | null>(env.BACKEND_URL+"/correio-franquia-context?franquiaId="+franquiaId);
  }

  getPesoCategorias(franquiaId:string){
    return this.http.get<CorreiosFranquiasContext.PesoCategoria[]>(env.BACKEND_URL+"/peso-categoria?franquiaId="+franquiaId);
  }

  cadastrarAtualizarPeso(payload:CorreiosFranquiasContext.PesoCategoria){
    return this.http.post<CorreiosFranquiasContext.PesoCategoria>(env.BACKEND_URL+"/peso-categoria",payload);
  }

  cadastrarAtualizarDimensoes(payload:CorreiosFranquiasContext.DimensaoCaixa){
    return this.http.post<CorreiosFranquiasContext.DimensaoCaixa>(env.BACKEND_URL+"/comprimento-caixa",payload);
  }

  getDimensoesByFranquiaId(franquiaId:string){
    return this.http.get<CorreiosFranquiasContext.DimensaoCaixa>(env.BACKEND_URL+"/comprimento-caixa?franquiaId="+franquiaId);
  }

  cadastrarAtualizarFaixa(faixa:CorreiosFranquiasContext.FaixaCep){
    return this.http.post<CorreiosFranquiasContext.FaixaCep>(env.BACKEND_URL+"/faixas-cep",faixa);
  }

  getFaixas(franquiaId:string){
    return this.http.get<CorreiosFranquiasContext.FaixaCep[]>(env.BACKEND_URL+"/faixas-cep?franquiaId="+franquiaId);
  }

  deletarFaixa(id:string){
    return this.http.delete<void>(env.BACKEND_URL+"/faixas-cep?id="+id);
  }


}
