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
  payload!:Readonly<Desconto.DescontoModel>;
  @Input({required:true})
  loading: boolean = false;
  @Output()
  onSave: EventEmitter<Desconto.DescontoModel> = new EventEmitter<Desconto.DescontoModel>();
  descontoSimples!:Desconto.DescontoSimplesProdutoModel;
  produtosTranslated:Desconto.ProdutoModel[] = [];
  constructor(private message:MessageService,private catalogService:CatalogoService){}
  ngOnInit(): void {
    this.descontoSimples = {
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
    if(this.descontoSimples?.percentDecimalDiscount){
      return Math.round(this.descontoSimples.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }

  set desconto(val:number){
    if(this.descontoSimples){
      this.descontoSimples.percentDecimalDiscount = val/100;
    }
  }
  validate(): boolean {
    if(!this.descontoSimples?.produto.systemId){
      this.message.add({
        severity:"Error",
        summary:"Produto catalogo inválido"
      })
      return false;
    }
    if(!this.descontoSimples.percentDecimalDiscount){
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
    const buffer = {
      ...this.payload,
      descontoSimples:this.descontoSimples
    }
    this.onSave.emit(buffer);
  }

}
