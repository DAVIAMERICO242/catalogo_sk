import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Sacola, SacolaService } from '../../../services/sacola.service';
import { ShippingCalculator, ShippingCalculatorService } from '../../../services/shipping-calculator.service';
import { SharedModule } from '../../../shared/shared.module';
import { Subject, Subscription, takeUntil } from 'rxjs';
import { Pedidos } from '../../../services/pedidos.service';
import { FreteContextService } from './frete-context.service';

@Component({
  selector: 'app-frete',
  imports: [SharedModule],
  templateUrl: './frete.component.html'
})
export class FreteComponent implements OnInit,OnDestroy {
  entregaLoja!:boolean;
  cep!:string;
  loadingValorFrete = false;
  propostaFrete!:ShippingCalculator.FreteResponse;
  pacOuSedex!:ShippingCalculator.PacOuSedex;
  pacSedexEnum = ShippingCalculator.PacOuSedex;
  subscriptions = new Subscription();
  constructor(
    private freteContext:FreteContextService
  ){}
  
  ngOnInit(): void {
    this.subscriptions.add(
      this.freteContext.cep$.subscribe((cep)=>{
        this.cep = cep;
      })
    );
    this.subscriptions.add(
      this.freteContext.entregaLoja$.subscribe((entregaLoja)=>{
        this.entregaLoja = entregaLoja;
      })
    );
    this.subscriptions.add(
      this.freteContext.loadingValorFrete$.subscribe((loading)=>{
        this.loadingValorFrete = loading;
      })
    );
    this.subscriptions.add(
      this.freteContext.propostaFrete$.subscribe((proposta)=>{
        if(proposta){
          this.propostaFrete = proposta;
        }
      })
    );
    this.subscriptions.add(
      this.freteContext.pacOuSedex$.subscribe((pacOuSedex)=>{
        if(pacOuSedex){
          this.pacOuSedex = pacOuSedex;
        }
      })
    )
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  onPacSedexChange(value:ShippingCalculator.PacOuSedex){
    this.freteContext.setPacOuSedex(value);
  }


}
