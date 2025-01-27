import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Desconto } from '../../../../services/descontos.service';
import { Produto, ProdutosService } from '../../../../services/produtos.service';
import { MessageService } from 'primeng/api';
import { UserService } from '../../../../services/user.service';
import { SharedModule } from '../../../../shared/shared.module';

@Component({
  selector: 'app-desconto-menor-valor-form',
  imports: [SharedModule],
  templateUrl: './desconto-menor-valor-form.component.html'
})
export class DescontoMenorValorFormComponent implements OnInit{
  @Input({required:true})
  payload!: Readonly<Desconto.DescontoModel>;
  @Input({required:true})
  loading: boolean = false;
  @Output()
  onSave: EventEmitter<Desconto.DescontoModel> = new EventEmitter<Desconto.DescontoModel>();
  loadingTermos = false;
  descontoMenorValor!:Desconto.DescontoMenorValorModel;
  termos:Produto.Termos = {
    categorias:[],
    grupos:[],
    linhas:[]
  };
  constructor(private message:MessageService,private produtoService:ProdutosService,private auth:UserService){}
  ngOnInit(): void {
    if(!this.payload.descontoMenorValor){
      this.descontoMenorValor = {
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
    }else{
      this.descontoMenorValor = {...this.payload.descontoMenorValor}
    }
    this.loadTermos();
  }
  get desconto(){
    if(this.descontoMenorValor?.percentDecimalDiscount){
      return Math.round(this.descontoMenorValor.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }
  set desconto(val:number){
    if(this.descontoMenorValor){
      this.descontoMenorValor.percentDecimalDiscount = val/100;
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
    if(this.descontoMenorValor){
      if(!this.descontoMenorValor.percentDecimalDiscount){
        this.message.add({
          severity:"error",
          summary:"O desconto não pode ser nulo"
        })
        return false;
      }
      if(this.descontoMenorValor.lowerQuantityLimitToApply<2){
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
      descontoMenorValor:this.descontoMenorValor
    }
    this.onSave.emit(buffer);
  }

}
