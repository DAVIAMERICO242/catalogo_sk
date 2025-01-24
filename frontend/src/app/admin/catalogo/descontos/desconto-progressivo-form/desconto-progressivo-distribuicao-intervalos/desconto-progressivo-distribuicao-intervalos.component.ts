import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SharedModule } from '../../../../../shared/shared.module';
import { Desconto } from '../../../../../services/descontos.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-desconto-progressivo-distribuicao-intervalos',
  imports: [SharedModule],
  templateUrl: './desconto-progressivo-distribuicao-intervalos.component.html'
})
export class DescontoProgressivoDistribuicaoIntervalosComponent {
  @Input({required:true})
  distribuicao:Desconto.IntervaloModel[] = [];
  @Output()
  distribuicaoChange = new EventEmitter<Desconto.IntervaloModel[]>();
  open = false;

  constructor(private message:MessageService) { }
  
  
  updatePorcentagem(index:number,val:number){
    this.distribuicao[index] = {
      ...this.distribuicao[index],
      percentDecimalDiscount:val/100
    }
    this.showTrueValue();
  }

  showTrueValue(){
    console.log(this.distribuicao)
  }

  round(val:number){
    return Math.round(val);
  }

  getMinQuantityForIndex(index:number){
    if(index!==0){
      const min = this.distribuicao[index-1].minQuantity + 1;
      return min;
    }else{
      return 9999;
    }
  }

  propagarOnClose(){
    this.distribuicaoChange.emit(this.distribuicao);
  }

  validarEFechar(){
    for(let i=0;i<this.distribuicao.length;i++){
      if(i>0){
        const previousQuantity = this.distribuicao[i-1].minQuantity;
        const previousDiscount = this.distribuicao[i-1].percentDecimalDiscount;
        if(this.distribuicao[i].minQuantity<=previousQuantity || this.distribuicao[i].percentDecimalDiscount<=previousDiscount){
          this.message.add({
            severity:"error",
            summary:"Há valores posteriores MENORES OU IGUAIS que anteriores"
          })
          return;
        }
      }
    }
    this.distribuicaoChange.emit(this.distribuicao);
    this.open=false;
  }

}
