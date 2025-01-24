import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DescontoForm } from '../desconto-form-interface';
import { Desconto } from '../../../../services/descontos.service';
import { MessageService } from 'primeng/api';
import { Catalogo, CatalogoService } from '../../../../services/catalogo.service';
import { SharedModule } from '../../../../shared/shared.module';

@Component({
  selector: 'app-desconto-simples-produto-form',
  imports: [SharedModule],
  templateUrl: './desconto-simples-produto-form.component.html'
})
export class DescontoSimplesProdutoFormComponent implements OnInit,DescontoForm {
  @Input({required:true})
  payload!: Desconto.DescontoModel;
  @Input({required:true})
  loading: boolean = false;
  @Output()
  onSave: EventEmitter<Desconto.DescontoModel> = new EventEmitter<Desconto.DescontoModel>();
  produtosTranslated:Desconto.ProdutoModel[] = [];
  constructor(private message:MessageService,private catalogService:CatalogoService){}
  ngOnInit(): void {
    this.payload={...this.payload};
    this.payload.descontoSimples = {
      systemId:"",
      percentDecimalDiscount:0,
      produto: {
        baseValue:0,
        catalogValue:0,
        nome:"",
        systemId:""
      }
    }
    this.catalogService.contextualCatalogoSub.value.forEach((e)=>{
       this.produtosTranslated.push({
           systemId:e.systemId,
           baseValue:e.produtoBase.preco,
           nome:e.produtoBase.descricao,
           catalogValue:e.valorCatalogo
       })
    })
  }
  get desconto(){
    if(this.payload.descontoSimples?.percentDecimalDiscount){
      return Math.round(this.payload.descontoSimples.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }

  set desconto(val:number){
    if(this.payload.descontoSimples){
      this.payload.descontoSimples.percentDecimalDiscount = val/100;
    }
  }
  validate(): boolean {
    if(!this.payload.descontoSimples?.produto.systemId){
      this.message.add({
        severity:"Error",
        summary:"Produto catalogo inválido"
      })
      return false;
    }
    if(!this.payload.descontoSimples.percentDecimalDiscount){
      this.message.add({
        severity:"Error",
        summary:"Desconto não pode ser nulo"
      })
      return false;
    }
    return true;
  }
  save(): void {
    if(!this.validate()){
      return;
    }
    this.onSave.emit(this.payload);
  }

}
