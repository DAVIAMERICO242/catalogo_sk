import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DescontoForm } from '../desconto-form-interface';
import { Desconto } from '../../../../services/descontos.service';
import { MessageService } from 'primeng/api';
import { Produto, ProdutosService } from '../../../../services/produtos.service';
import { UserService } from '../../../../services/user.service';
import { SharedModule } from '../../../../shared/shared.module';

@Component({
  selector: 'app-desconto-simples-termo-form',
  imports: [SharedModule],
  templateUrl: './desconto-simples-termo-form.component.html'
})
export class DescontoSimplesTermoFormComponent implements OnInit,DescontoForm {
  @Input({required:true})
  payload!:Desconto.DescontoModel;
  @Input({required:true})
  loading: boolean = false;
  @Output()
  onSave: EventEmitter<Desconto.DescontoModel> = new EventEmitter<Desconto.DescontoModel>();
  loadingTermos = false;
  termos!:Produto.Termos;
  constructor(private message:MessageService,private produtoService:ProdutosService,private auth:UserService){}

  ngOnInit(): void {
    this.payload={...this.payload};
    this.payload.descontoSimplesTermo = {
      delimitedCategorias:[],
      delimitedGrupos:[],
      delimitedLinhas:[],
      excludedCategorias:[],
      excludedGrupos:[],
      excludedLinhas:[],
      percentDecimalDiscount:0,
      systemId:""
    }
    this.loadTermos();
  }

  loadTermos(){
    const franquiaId = this.auth.getContext()?.franquia.systemId;
    if(franquiaId){
      this.loadingTermos = true;
      this.produtoService.getTermos(franquiaId).subscribe({
        next:(data)=>{
          this.termos = data;
        },
        error:()=>{
          alert("Erro ao carregar termos")
        }
      })
    }
  }
  get desconto(){
    if(this.payload.descontoSimplesTermo?.percentDecimalDiscount){
      return Math.round(this.payload.descontoSimplesTermo.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }

  set desconto(val:number){
    if(this.payload.descontoSimplesTermo){
      this.payload.descontoSimplesTermo.percentDecimalDiscount = val/100;
    }
  }

  validate(): boolean {
    if(
      !this.payload.descontoSimplesTermo?.delimitedCategorias.length
      && !this.payload.descontoSimplesTermo?.delimitedGrupos.length
      && !this.payload.descontoSimplesTermo?.delimitedLinhas.length
      && !this.payload.descontoSimplesTermo?.excludedCategorias.length
      && !this.payload.descontoSimplesTermo?.excludedGrupos.length
      && !this.payload.descontoSimplesTermo?.excludedLinhas.length
    ){
      this.message.add({
        severity:"error",
        summary:"Nenhuma limitação de termo encontrada!"
      })
      return false;
    }
    if(!this.payload.descontoSimplesTermo.percentDecimalDiscount){
      this.message.add({
        severity:"error",
        summary:"O desconto não pode ser nulo"
      })
    }
    return true;
  }
  save(): void {
    if(!this.validate()){
      return
    }
    this.onSave.emit(this.payload);
  }

}
