import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DescontoForm } from '../desconto-form-interface';
import { Desconto } from '../../../../services/descontos.service';
import { Produto, ProdutosService } from '../../../../services/produtos.service';
import { MessageService } from 'primeng/api';
import { UserService } from '../../../../services/user.service';
import { SharedModule } from '../../../../shared/shared.module';
import { DescontoProgressivoDistribuicaoIntervalosComponent } from "./desconto-progressivo-distribuicao-intervalos/desconto-progressivo-distribuicao-intervalos.component";

@Component({
  selector: 'app-desconto-progressivo-form',
  imports: [SharedModule, DescontoProgressivoDistribuicaoIntervalosComponent],
  templateUrl: './desconto-progressivo-form.component.html'
})
export class DescontoProgressivoFormComponent implements OnInit,DescontoForm {
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
    this.payload.descontoProgressivo = {
      delimitedCategorias:[],
      delimitedGrupos:[],
      delimitedLinhas:[],
      excludedCategorias:[],
      excludedGrupos:[],
      excludedLinhas:[],
      systemId:"",
      intervalos:[
        {
          minQuantity:1,
          percentDecimalDiscount:0.2
        },{
          minQuantity:2,
          percentDecimalDiscount:0.25
        }
      ]
    }
    this.loadTermos();
  }
  // get desconto(){
  //   if(this.payload.descontoProgressivo?.percentDecimalDiscount){
  //     return Math.round(this.payload.descontoMenorValor.percentDecimalDiscount * 100);
  //   }else{
  //     return 0;
  //   }
  // }
  // set desconto(val:number){
  //   if(this.payload.descontoMenorValor){
  //     this.payload.descontoMenorValor.percentDecimalDiscount = val/100;
  //   }
  // }
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
    if(this.payload.descontoProgressivo){
      if(!this.payload.descontoProgressivo.intervalos.length){
        this.message.add({
          severity:"error",
          summary:"Nenhum intervalo configurado"
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

  changeDistribuicao(intervals:Desconto.IntervaloModel[]){
    if(this.payload.descontoProgressivo?.intervalos){
      this.payload.descontoProgressivo.intervalos = intervals;
      console.log("DISTRIBUIÇÃO SALVA NO PAI")
      console.log(intervals);
    }
  }

}
