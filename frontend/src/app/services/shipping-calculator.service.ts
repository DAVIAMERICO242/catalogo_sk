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
    pacSedex?:null | undefined | PacOuSedex;//opcional pq pode ser calculado pela faixa de cep
    cep:string;
    lojaId:string;
    produtos:Pedidos.ProdutoPedido[]
  }
  export interface HowShouldBeCalculatedResponse{
    tipo:TipoCalculo
  }
  export interface FreteResponse{
    valor:number;
    prazoEmDias:number;
  }
}

@Injectable({
  providedIn: 'root'
})
export class ShippingCalculatorService {

  constructor(private http:HttpClient) {}

  getHowShoulBeCalculated(payload:ShippingCalculator.ShippingCalculationRequest){
    return this.http.post<ShippingCalculator.HowShouldBeCalculatedResponse>(env.BACKEND_URL+"/shipping-calculator/how-should-be-calculated",payload);
  }

  getValorFreteSemDesconto(payload:ShippingCalculator.ShippingCalculationRequest){//o desconto vai ser aplicado no contexto de desconto da sacola
    return this.http.post<ShippingCalculator.FreteResponse>(env.BACKEND_URL+"/shipping-calculator",payload);
  }
}
