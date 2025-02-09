import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Sacola, SacolaService } from '../../../services/sacola.service';
import { DescontosAplicadosComponent } from "../descontos-aplicados/descontos-aplicados.component";
import { Button } from 'primeng/button';
import { Desconto } from '../../../services/descontos.service';
import { filter, Subscription, take } from 'rxjs';
import { SharedModule } from '../../../shared/shared.module';
import { SacolaUiContextService } from '../sacola-ui-context.service';

@Component({
  selector: 'app-valores-details',
  imports: [DescontosAplicadosComponent,SharedModule],
  templateUrl: './valores-details.component.html'
})
export class ValoresDetailsComponent implements OnInit,OnDestroy {
  @Input({required:true})
  rawSacola!:Sacola.RawSacola;
  total!:number;
  descontosAplicados!:Desconto.DescontoAplicado[];
  subscriptions = new Subscription();

  constructor(protected sacolaContext:SacolaUiContextService){}
  
  ngOnInit(): void {

    this.subscriptions.add(this.sacolaContext.descontosAplicados$.subscribe((data)=>{
      let bruto = 0;
      for(let produto of this.rawSacola.produtos){
        for(let variacao of produto.variacoesCompradas){
          bruto = bruto + produto.preco;
        }
      }
        this.descontosAplicados = data;
        const descontoTotal = this.descontosAplicados.reduce((a,b)=>a+b.valorAplicado,0);
        this.total = bruto - descontoTotal;
        console.log(this.total)
    }));
  }
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
