import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Desconto } from '../../../../services/descontos.service';
import { MessageService } from 'primeng/api';
import { SharedModule } from '../../../../shared/shared.module';
import { DescontoForm } from '../desconto-form-interface';

@Component({
  selector: 'app-desconto-frete-form',
  imports: [SharedModule],
  templateUrl: './desconto-frete-form.component.html'
})
export class DescontoFreteFormComponent implements OnInit,DescontoForm{
  @Input({required:true})
  loading = false;
  @Input({required:true})
  payload!:Desconto.DescontoModel;
  @Output()
  onSave = new EventEmitter<Desconto.DescontoModel>();

  constructor(private message:MessageService){}
  
  ngOnInit(): void {
    this.payload={...this.payload};
    this.payload.descontoFrete = {
      lowerValueLimitToApply:0,
      percentDecimalDiscount:0,
      systemId:""
    }
  }


  get desconto(){
    if(this.payload.descontoFrete?.percentDecimalDiscount){
      return Math.round(this.payload.descontoFrete.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }

  set desconto(val:number){
    if(this.payload.descontoFrete){
      this.payload.descontoFrete.percentDecimalDiscount = val/100;
    }
  }

  validate(){
    if(!this.payload.descontoFrete?.percentDecimalDiscount){
      this.message.add({
        severity:"error",
        summary:"O desconto não pode ser nulo"
      })
      return false;
    }
    return true;
  }

  save(){
    if(!this.validate()){
      return;
    }
    this.onSave.emit(this.payload);
  }
}
