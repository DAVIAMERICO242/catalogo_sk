import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject, takeUntil } from 'rxjs';
import { ShippingCalculator, ShippingCalculatorService } from '../../../services/shipping-calculator.service';
import { Pedidos } from '../../../services/pedidos.service';
import { Sacola, SacolaService } from '../../../services/sacola.service';

export interface FreteEmissionSignature{
  tipo:Pedidos.TipoFrete,//caso a busca for em loja
  valorFrete:number
}
@Injectable({
  providedIn: 'root'
})

export class FreteContextService {
  private prevFreteRequestDestroyer$ = new Subject<void>();
  private entregaLojaSub = new BehaviorSubject<boolean>(false);
  entregaLoja$ = this.entregaLojaSub.asObservable();
  private cepSub = new BehaviorSubject<string>("");
  cep$ = this.cepSub.asObservable();
  private cepAnterior = "";
  private propostaFreteSub = new BehaviorSubject<ShippingCalculator.FreteResponse | undefined>(undefined);
  propostaFrete$ = this.propostaFreteSub.asObservable();
  private pacOuSedexSub = new BehaviorSubject<ShippingCalculator.PacOuSedex | undefined>(undefined);
  pacOuSedex$ = this.pacOuSedexSub.asObservable();
  private freteEmissorSub = new Subject<FreteEmissionSignature>();
  freteEmissor$ = this.freteEmissorSub.asObservable();
  private lastFreteEmission!:FreteEmissionSignature;//serve pra recuperar o estado anterior quando o fluxo de loja é marcado e desmarcado
  private loadingValorFreteSub = new BehaviorSubject<boolean>(false);
  loadingValorFrete$ = this.loadingValorFreteSub.asObservable();
  
  constructor(
    private sacolaService:SacolaService,
    private shippingCalculator:ShippingCalculatorService
  ) {}

  emitFreteChange(emission:FreteEmissionSignature){
    this.lastFreteEmission = emission;
    this.freteEmissorSub.next(emission);
  }

  setCep(val:string){
    this.cepAnterior = this.cepSub.getValue();
    this.cepSub.next(val);
  }

  setColetaLoja(val:boolean){
    this.entregaLojaSub.next(val);
    if(this.lastFreteEmission && !val){
      this.emitFreteChange(this.lastFreteEmission);
    }
  }

  setPacOuSedex(value:ShippingCalculator.PacOuSedex){
    if(value===ShippingCalculator.PacOuSedex.PAC){
      this.pacOuSedexSub.next(ShippingCalculator.PacOuSedex.PAC);
      const proposta = this.propostaFreteSub.getValue();
      if(proposta){
        this.emitFreteChange({
          tipo:Pedidos.TipoFrete.PAC,
          valorFrete:proposta.valorPac
        });
      }
    }
    if(value===ShippingCalculator.PacOuSedex.SEDEX){
      this.pacOuSedexSub.next(ShippingCalculator.PacOuSedex.SEDEX);
      const proposta = this.propostaFreteSub.getValue();
      if(proposta){
        this.emitFreteChange({
          tipo:Pedidos.TipoFrete.SEDEX,
          valorFrete:proposta.valorSedex
        });
      }
    }
  }

  setValorFrete(sacola:Sacola.SacolaModel,forceEvenCepDidntChange:boolean){
    if(this.cepAnterior===this.cepSub.getValue() && !forceEvenCepDidntChange){
      return;
    }
    this.prevFreteRequestDestroyer$.next();
    const rawSacola = this.sacolaService.mapModelToRawSacola(sacola);
    const payload:ShippingCalculator.ShippingCalculationRequest = {
      cep:this.cepSub.getValue(),
      lojaId:sacola.loja.systemId,
      produtos:rawSacola.produtos
    }
    this.loadingValorFreteSub.next(true);
    this.shippingCalculator.getValorFreteSemDesconto(payload).pipe(takeUntil(this.prevFreteRequestDestroyer$)).subscribe({
      next:(data)=>{
        this.loadingValorFreteSub.next(false);
        this.propostaFreteSub.next(data);
        if(data.tipo==ShippingCalculator.TipoCalculo.FAIXA_CEP){
          this.emitFreteChange(
            {
              tipo:Pedidos.TipoFrete.FAIXA_CEP,
              valorFrete:data.valorFaixaCep
            }
          );
        }
        if(data.tipo===ShippingCalculator.TipoCalculo.CORREIOS){
          this.pacOuSedexSub.next(ShippingCalculator.PacOuSedex.PAC);
          this.emitFreteChange(
            {
              tipo:Pedidos.TipoFrete.PAC,
              valorFrete:data.valorPac
            }
          );
        }
      }
    })
  }


}
