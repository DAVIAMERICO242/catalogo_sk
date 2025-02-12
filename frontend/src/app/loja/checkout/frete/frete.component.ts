import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Sacola, SacolaService } from '../../../services/sacola.service';
import { ShippingCalculator, ShippingCalculatorService } from '../../../services/shipping-calculator.service';
import { SharedModule } from '../../../shared/shared.module';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-frete',
  imports: [SharedModule],
  templateUrl: './frete.component.html'
})
export class FreteComponent {
  private prevRequestDestroyer$ = new Subject<void>();
  @Input({required:true})
  cep!:string;
  @Input({required:true})
  sacola!:Sacola.SacolaModel;
  loadingValorFrete = false;
  frete!:ShippingCalculator.FreteResponse;
  @Output()
  onFreteCalculation = new EventEmitter<typeof this.frete>();
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
        this.frete = data;
      }
    })
  }


}
