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
  payload!:Readonly<Desconto.DescontoModel>;
  descontoFrete!:Desconto.DescontoFreteModel;
  @Output()
  onSave = new EventEmitter<Desconto.DescontoModel>();

  constructor(private message:MessageService){}
  
  ngOnInit(): void {
    if(!this.payload.descontoFrete){
      this.descontoFrete = {
        lowerValueLimitToApply:0,
        percentDecimalDiscount:0,
        systemId:""
      }
    }else{
      this.descontoFrete = {...this.payload.descontoFrete}
    }
  }


  get desconto(){
    if(this.descontoFrete?.percentDecimalDiscount){
      return Math.round(this.descontoFrete.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }

  set desconto(val:number){
    if(this.descontoFrete){
      this.descontoFrete.percentDecimalDiscount = val/100;
    }
  }

  validate(){
    if(!this.descontoFrete?.percentDecimalDiscount){
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
    const buffer = {
      ...this.payload,
      descontoFrete:this.descontoFrete
    }
    this.onSave.emit(buffer);
  }
}
