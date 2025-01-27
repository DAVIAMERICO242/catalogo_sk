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
  payload!:Readonly<Desconto.DescontoModel>;
  @Input({required:true})
  loading: boolean = false;
  @Output()
  onSave: EventEmitter<Desconto.DescontoModel> = new EventEmitter<Desconto.DescontoModel>();
  loadingTermos = false;
  termos:Produto.Termos = {
    categorias:[],
    grupos:[],
    linhas:[]
  };
  descontoSimplesTermo!:Desconto.DescontoSimplesTermoModel;
  constructor(private message:MessageService,private produtoService:ProdutosService,private auth:UserService){}

  ngOnInit(): void {
    if(!this.payload.descontoSimplesTermo){
      this.descontoSimplesTermo = {
        delimitedCategorias:[],
        delimitedGrupos:[],
        delimitedLinhas:[],
        excludedCategorias:[],
        excludedGrupos:[],
        excludedLinhas:[],
        percentDecimalDiscount:0,
        systemId:""
      }
    }else{
      this.descontoSimplesTermo = {...this.payload.descontoSimplesTermo}
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
    if(this.descontoSimplesTermo?.percentDecimalDiscount){
      return Math.round(this.descontoSimplesTermo.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }

  set desconto(val:number){
    if(this.descontoSimplesTermo){
      this.descontoSimplesTermo.percentDecimalDiscount = val/100;
    }
  }

  validate(): boolean {
    if(
      !this.descontoSimplesTermo?.delimitedCategorias.length
      && !this.descontoSimplesTermo?.delimitedGrupos.length
      && !this.descontoSimplesTermo?.delimitedLinhas.length
      && !this.descontoSimplesTermo?.excludedCategorias.length
      && !this.descontoSimplesTermo?.excludedGrupos.length
      && !this.descontoSimplesTermo?.excludedLinhas.length
    ){
      this.message.add({
        severity:"error",
        summary:"Nenhuma limitação de termo encontrada!"
      })
      return false;
    }
    if(!this.descontoSimplesTermo.percentDecimalDiscount){
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
    const buffer = {
      ...this.payload,
      descontoSimplesTermo:this.descontoSimplesTermo
    }
    this.onSave.emit(buffer);
  }

}
