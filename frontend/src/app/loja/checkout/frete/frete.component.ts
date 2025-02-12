import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Sacola, SacolaService } from '../../../services/sacola.service';
import { ShippingCalculator, ShippingCalculatorService } from '../../../services/shipping-calculator.service';
import { SharedModule } from '../../../shared/shared.module';
import { Subject, takeUntil } from 'rxjs';
import { Pedidos } from '../../../services/pedidos.service';

export interface FreteEmissionSignature{
  tipo:Pedidos.TipoFrete,//caso a busca for em loja
  valorFrete:number
}
@Component({
  selector: 'app-frete',
  imports: [SharedModule],
  templateUrl: './frete.component.html'
})
export class FreteComponent {
  private prevRequestDestroyer$ = new Subject<void>();
  @Input({required:true})
  entregaLoja!:boolean;
  @Input({required:true})
  cep!:string;
  @Input({required:true})
  sacola!:Sacola.SacolaModel;
  loadingValorFrete = false;
  propostaFrete!:ShippingCalculator.FreteResponse;
  @Output()
  onFreteChange = new EventEmitter<FreteEmissionSignature>();
  constructor(
    private shippingCalculator:ShippingCalculatorService,
    private sacolaService:SacolaService
  ){}

  getValorFrete(){
    this.prevRequestDestroyer$.next();
    const rawSacola = this.sacolaService.mapModelToRawSacola(this.sacola);
    const payload:ShippingCalculator.ShippingCalculationRequest = {
      cep:this.cep,
      lojaId:this.sacola.loja.systemId,
      produtos:rawSacola.produtos
    }
    this.loadingValorFrete = true;
    this.shippingCalculator.getValorFreteSemDesconto(payload).pipe(takeUntil(this.prevRequestDestroyer$)).subscribe({
      next:(data)=>{
        this.loadingValorFrete = false;
        this.propostaFrete = data;
        if(data.tipo==ShippingCalculator.TipoCalculo.FAIXA_CEP){
          this.onFreteChange.emit({
            tipo:Pedidos.TipoFrete.FAIXA_CEP,
            valorFrete:data.valorFaixaCep
          })
        }
      }
    })
  }

  onPacSedexChange(value:ShippingCalculator.PacOuSedex){
    if(value===ShippingCalculator.PacOuSedex.PAC){
      this.onFreteChange.emit({
        tipo:Pedidos.TipoFrete.PAC,
        valorFrete:this.propostaFrete.valorPac
      })
    }
    if(value===ShippingCalculator.PacOuSedex.SEDEX){
      this.onFreteChange.emit({
        tipo:Pedidos.TipoFrete.SEDEX,
        valorFrete:this.propostaFrete.valorSedex
      })
    }
  }


}
