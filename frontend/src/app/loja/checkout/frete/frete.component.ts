import { Component, Input } from '@angular/core';
import { Sacola, SacolaService } from '../../../services/sacola.service';
import { ShippingCalculator, ShippingCalculatorService } from '../../../services/shipping-calculator.service';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-frete',
  imports: [SharedModule],
  templateUrl: './frete.component.html'
})
export class FreteComponent {
  @Input({required:true})
  cep!:string;
  @Input({required:true})
  sacola!:Sacola.SacolaModel;
  loadingTipoFrete = false;
  frete!:ShippingCalculator.FreteResponse;
  constructor(
    private shippingCalculator:ShippingCalculatorService,
    private sacolaService:SacolaService
  ){}

  getTipoFrete(){
    const rawSacola = this.sacolaService.mapModelToRawSacola(this.sacola);
    const payload:ShippingCalculator.ShippingCalculationRequest = {
      cep:this.cep,
      lojaId:this.sacola.loja.systemId,
      produtos:rawSacola.produtos
    }
    this.loadingTipoFrete = true;
    this.shippingCalculator.getValorFreteSemDesconto(payload).subscribe({
      next:(data)=>{
        this.frete = data;
      }
    })
  }


}
