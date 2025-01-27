import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DescontoForm } from '../desconto-form-interface';
import { Desconto } from '../../../../services/descontos.service';
import { MessageService } from 'primeng/api';
import { SharedModule } from '../../../../shared/shared.module';
import { Produto, ProdutosService } from '../../../../services/produtos.service';
import { UserService } from '../../../../services/user.service';

@Component({
  selector: 'app-desconto-maior-valor-form',
  imports: [SharedModule],
  templateUrl: './desconto-maior-valor-form.component.html'
})
export class DescontoMaiorValorFormComponent implements OnInit,DescontoForm {
  @Input({required:true})
  payload!: Readonly<Desconto.DescontoModel>;
  @Input({required:true})
  loading: boolean = false;
  @Output()
  onSave: EventEmitter<Desconto.DescontoModel> = new EventEmitter<Desconto.DescontoModel>();
  loadingTermos = false;
  descontoMaiorValor!:Desconto.DescontoMaiorValorModel;
  termos:Produto.Termos = {
    categorias:[],
    grupos:[],
    linhas:[]
  };
  constructor(private message:MessageService,private produtoService:ProdutosService,private auth:UserService){
    
  }
  ngOnInit(): void {
    this.descontoMaiorValor = {
      delimitedCategorias:[],
      delimitedGrupos:[],
      delimitedLinhas:[],
      excludedCategorias:[],
      excludedGrupos:[],
      excludedLinhas:[],
      lowerQuantityLimitToApply:2,
      percentDecimalDiscount:0,
      systemId:""
    }
    this.loadTermos();
  }
  get desconto(){
    if(this.descontoMaiorValor?.percentDecimalDiscount){
      return Math.round(this.descontoMaiorValor.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }
  set desconto(val:number){
    if(this.descontoMaiorValor){
      this.descontoMaiorValor.percentDecimalDiscount = val/100;
    }
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
  validate(): boolean {
    if(this.descontoMaiorValor){
      if(!this.descontoMaiorValor.percentDecimalDiscount){
        this.message.add({
          severity:"error",
          summary:"O desconto não pode ser nulo"
        })
        return false;
      }
      if(this.descontoMaiorValor.lowerQuantityLimitToApply<2){
        this.message.add({
          severity:"error",
          summary:"A quantidade mínima do carrinho é 2"
        })
        return false;
      }
    }
    return true;
  }
  save(): void {
    if(!this.validate()){
      return;
    }
    const buffer = {
      ...this.payload,
      descontoMaiorValor:this.descontoMaiorValor
    }
    this.onSave.emit(buffer);
  }

}
