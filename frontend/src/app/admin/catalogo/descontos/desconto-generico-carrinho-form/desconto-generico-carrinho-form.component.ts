import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DescontoForm } from '../desconto-form-interface';
import { Desconto } from '../../../../services/descontos.service';
import { MessageService } from 'primeng/api';
import { SharedModule } from '../../../../shared/shared.module';

@Component({
  selector: 'app-desconto-generico-carrinho-form',
  imports: [SharedModule],
  templateUrl: './desconto-generico-carrinho-form.component.html'
})
export class DescontoGenericoCarrinhoFormComponent implements OnInit,DescontoForm {
  @Input({required:true})
  payload!:Readonly<Desconto.DescontoModel>;
  @Input({required:true})
  loading: boolean = false;
  @Output()
  onSave = new EventEmitter<Desconto.DescontoModel>();

  descontoGenericoCarrinho!:Desconto.DescontoGenericoCarrinhoModel;

  constructor(private message:MessageService){}

  ngOnInit(): void {
    if(!this.payload.descontoGenericoCarrinho){
      this.descontoGenericoCarrinho = {
        minValue:1,
        percentDecimalDiscount:0,
        systemId:""
      }
    }else{
      this.descontoGenericoCarrinho = {...this.payload.descontoGenericoCarrinho};
    }
  }

  get desconto(){
    if(this.descontoGenericoCarrinho?.percentDecimalDiscount){
      return Math.round(this.descontoGenericoCarrinho.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }

  set desconto(val:number){
    if(this.descontoGenericoCarrinho){
      this.descontoGenericoCarrinho.percentDecimalDiscount = val/100;
    }
  }

  validate(){
    if(!this.descontoGenericoCarrinho?.percentDecimalDiscount){
      this.message.add({
        severity:"error",
        summary:"O desconto não pode ser nulo"
      })
      return false;
    }
    return true;
  }

  save(): void {
    if(!this.validate()){
      return;
    }
    const buffer = {
      ...this.payload,
     descontoGenericoCarrinho: this.descontoGenericoCarrinho
    }
    this.onSave.emit(buffer);
  }
}



