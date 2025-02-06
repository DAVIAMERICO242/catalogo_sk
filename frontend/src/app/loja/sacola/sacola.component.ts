import { Component, OnDestroy, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { Sacola, SacolaService } from '../../services/sacola.service';
import { LojaContextService } from '../loja-context.service';
import { Loja } from '../../services/loja.service';
import { filter, Subscriber, Subscription, take } from 'rxjs';

@Component({
  selector: 'app-sacola',
  imports: [SharedModule],
  templateUrl: './sacola.component.html'
})
export class SacolaComponent implements OnInit,OnDestroy {
  loja!:Loja.Loja;
  beautySacola!:Sacola.BeautySacola|undefined;
  totaisItensSacola!:number;
  subscriptions = new Subscription();
  open = false;
  constructor(protected sacolaService:SacolaService,private lojaContext:LojaContextService){}
  ngOnInit(): void {
    this.lojaContext.loja$.pipe(filter(loja => !!loja), take(1)).subscribe((loja)=>
      {
          if(loja){
            this.loja = loja;
            this.setSacola();
          }
      });
    this.subscriptions.add(this.sacolaService.onSacolaChange$.subscribe(()=>{
      this.setSacola();
    }));
    this.subscriptions.add(this.sacolaService.open$.subscribe((val)=>{
      this.open = val;
    }));
     
  }
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
      
  setSacola(){
    this.beautySacola = this.sacolaService.getBeautySacolaForLoja({
      nome:this.loja.loja,
      slug:this.loja.slug,
      systemId:this.loja.systemId
    });
    if(this.beautySacola?.itens.length){
      this.totaisItensSacola = this.beautySacola?.itens.reduce((a,b)=>a+b.quantidade,0);
    }
    console.log(this.beautySacola)
  }

}
