import { Component, Input } from '@angular/core';
import { Sacola, SacolaService } from '../../../services/sacola.service';
import { ShippingCalculator, ShippingCalculatorService } from '../../../services/shipping-calculator.service';

@Component({
  selector: 'app-frete',
  imports: [],
  templateUrl: './frete.component.html'
})
export class FreteComponent {
  @Input({required:true})
  cep!:string;
  @Input({required:true})
  sacola!:Sacola.SacolaModel;
  loadingTipoFrete = false;
  tipoFrete!:ShippingCalculator.TipoCalculo;

  constructor(
    private shippingCalculator:ShippingCalculatorService,
    private sacolaService:SacolaService
  ){}

  getTipoFrete(){
    const rawSacola = this.sacolaService.mapModelToRawSacola(this.sacola);
    const payload:ShippingCalculator.ShippingCalculationRequest = {
      cep:this.cep,
      lojaId:this.sacola.loja.systemId,
      produtos:rawSacola.produtos,
      pacSedex:null
    }
    this.loadingTipoFrete = true;
    this.shippingCalculator.getHowShoulBeCalculated(payload).subscribe({
      next:(data)=>{
        this.tipoFrete = data.tipo;
      }
    })
  }


}
