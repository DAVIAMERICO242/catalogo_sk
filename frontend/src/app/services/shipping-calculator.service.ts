import { Injectable } from '@angular/core';
import { Pedidos } from './pedidos.service';
import { HttpClient } from '@angular/common/http';
import { env } from '../../env';

export namespace ShippingCalculator{
  export enum PacOuSedex{
    PAC="PAC",
    SEDEX="SEDEX"
  }
  export enum TipoCalculo{
    FAIXA_CEP="FAIXA_CEP",
    CORREIOS="CORREIOS"
  }
  export interface ShippingCalculationRequest{
    cep:string;
    lojaId:string;
    produtos:Pedidos.ProdutoPedido[]
  }
  export interface FreteResponse{
    tipo:TipoCalculo;
    valorFaixaCep:number;
    prazoEmDiasFaixaCep:number;
    valorPac:number;
    prazoEmDiasPac:number;
    valorSedex:number;
    prazoEmDiasSedex:number;
  }
}

@Injectable({
  providedIn: 'root'
})
export class ShippingCalculatorService {

  constructor(private http:HttpClient) {}


  getValorFreteSemDesconto(payload:ShippingCalculator.ShippingCalculationRequest){//o desconto vai ser aplicado no contexto de desconto da sacola
    return this.http.post<ShippingCalculator.FreteResponse>(env.BACKEND_URL+"/shipping-calculator",payload);
  }
}
