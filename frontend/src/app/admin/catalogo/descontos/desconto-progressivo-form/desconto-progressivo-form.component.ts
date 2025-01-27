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
  payload!:Readonly<Desconto.DescontoModel>;
  @Input({required:true})
  loading: boolean = false;
  @Output()
  onSave: EventEmitter<Desconto.DescontoModel> = new EventEmitter<Desconto.DescontoModel>();
  descontoProgressivo!:Desconto.DescontoProgressivoModel;
  loadingTermos = false;
  termos:Produto.Termos = {
    categorias:[],
    grupos:[],
    linhas:[]
  };
  constructor(private message:MessageService,private produtoService:ProdutosService,private auth:UserService){}
  ngOnInit(): void {
    this.descontoProgressivo = {
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
    if(this.descontoProgressivo){
      if(!this.descontoProgressivo.intervalos.length){
        this.message.add({
          severity:"error",
          summary:"Nenhum intervalo configurado"
        })
        return false;
      }
    }
    const distribuicao = this.descontoProgressivo?.intervalos;
    if(distribuicao){
      for(let i=0;i<distribuicao.length;i++){
        if(i>0){
          const previousQuantity = distribuicao[i-1].minQuantity;
          const previousDiscount = distribuicao[i-1].percentDecimalDiscount;
          if(distribuicao[i].minQuantity<=previousQuantity || distribuicao[i].percentDecimalDiscount<=previousDiscount){
            this.message.add({
              severity:"error",
              summary:"Há valores posteriores MENORES OU IGUAIS que anteriores"
            })
            return false;
          }
        }
      }
    }
    if(!distribuicao?.length){
      this.message.add({
        severity:"error",
        summary:"Nenhuma intervalo encontrado"
      })
    }
    return true;
  }
  save(): void {
    if(!this.validate()){
      return;
    }
    const buffer={
      ...this.payload,
      descontoProgressivo:this.descontoProgressivo
    }
    this.onSave.emit(buffer);
  }

  changeDistribuicao(intervals:Desconto.IntervaloModel[]){
    if(this.descontoProgressivo?.intervalos){
      this.descontoProgressivo.intervalos = intervals;
      console.log("DISTRIBUIÇÃO SALVA NO PAI")
      console.log(intervals);
    }
  }

}
