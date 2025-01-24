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
  payload!: Desconto.DescontoModel;
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
  constructor(private message:MessageService,private produtoService:ProdutosService,private auth:UserService){}
  ngOnInit(): void {
    this.payload = {...this.payload};
    this.payload.descontoMenorValor = {
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
    if(this.payload.descontoMenorValor?.percentDecimalDiscount){
      return Math.round(this.payload.descontoMenorValor.percentDecimalDiscount * 100);
    }else{
      return 0;
    }
  }
  set desconto(val:number){
    if(this.payload.descontoMenorValor){
      this.payload.descontoMenorValor.percentDecimalDiscount = val/100;
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
    if(this.payload.descontoMenorValor){
      if(!this.payload.descontoMenorValor.percentDecimalDiscount){
        this.message.add({
          severity:"error",
          summary:"O desconto não pode ser nulo"
        })
        return false;
      }
      if(this.payload.descontoMenorValor.lowerQuantityLimitToApply<2){
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
    this.onSave.emit(this.payload);
  }

}
