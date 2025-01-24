import { Component, Input, OnInit } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { Desconto, DescontosService } from '../../../../services/descontos.service';
import { DescontosBeautyNomes } from '../descontos.component';
import { UserService } from '../../../../services/user.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { DescontoFreteFormComponent } from "../desconto-frete-form/desconto-frete-form.component";
import { DescontoGenericoCarrinhoFormComponent } from "../desconto-generico-carrinho-form/desconto-generico-carrinho-form.component";
import { DescontoSimplesProdutoFormComponent } from "../desconto-simples-produto-form/desconto-simples-produto-form.component";
import { DescontoSimplesTermoFormComponent } from "../desconto-simples-termo-form/desconto-simples-termo-form.component";
import { DescontoMaiorValorFormComponent } from "../desconto-maior-valor-form/desconto-maior-valor-form.component";
import { DescontoMenorValorFormComponent } from "../desconto-menor-valor-form/desconto-menor-valor-form.component";
import { DescontoProgressivoFormComponent } from "../desconto-progressivo-form/desconto-progressivo-form.component";

@Component({
  selector: 'app-novo-desconto',
  imports: [SharedModule, DescontoFreteFormComponent, DescontoGenericoCarrinhoFormComponent, DescontoSimplesProdutoFormComponent, DescontoSimplesTermoFormComponent, DescontoMaiorValorFormComponent, DescontoMenorValorFormComponent, DescontoProgressivoFormComponent],
  templateUrl: './novo-desconto.component.html'
})
export class NovoDescontoComponent implements OnInit {
  open = false;
  @Input({required:true})
  descontoTipos:DescontosBeautyNomes[] = [];
  payload!:Desconto.DescontoModel;
  focusedTipo!:DescontosBeautyNomes;
  loadingSave = false;

  DescontoTipoEnum = Desconto.DescontoTipo;

  constructor(private auth:UserService,private message:MessageService,private descontoService:DescontosService) {
    
  }
  
  ngOnInit(): void {
    this.focusedTipo = this.descontoTipos[0];
    this.definePayLoadModel();
  }

  definePayLoadModel(){
    const contextualLoja = this.auth.getContext()?.loja;
    if(contextualLoja){
      this.payload = {
        ...this.payload,
        tipo:this.focusedTipo.pure_name,
        systemId: '',
        loja: {
          nome:contextualLoja?.nome,
          slug:contextualLoja?.slug,
          systemId:contextualLoja?.systemId
        },
        isActive: true,
        expiresAt: new Date(),
      };
    }
  }

  manageTipoChange(){
    this.definePayLoadModel();
  }

  // get totalCartDecimalPercentDiscountView(): number {
  //   if(this.payload.totalCartDecimalPercentDiscount){
  //     return this.payload.totalCartDecimalPercentDiscount*100;
  //   }else{
  //     return 0;
  //   }
  // }

  // set totalCartDecimalPercentDiscountView(val:number){
  //   this.payload.totalCartDecimalPercentDiscount = val / 100;
  // }

  // testar(){
  //   alert(this.payload.totalCartDecimalPercentDiscount);
  // }


  childSave(payload:Desconto.DescontoModel){
    this.payload = payload;
    this.submit();
  }

  private submit(){//o resto é validado no componente relativo ao tipo de desconto
    if(!this.payload.nome){
      this.message.add({
        severity:"error",
        summary:"O desconto precisa de um nome"
      })
      return;
    }
    if(!this.payload.tipo){
      this.message.add({
        severity:"error",
        summary:"Tipo inválido"
      })
      return;
    }
    if(!this.payload.loja){
      this.message.add({
        severity:"error",
        summary:"Loja inválida"
      })
      return;
    }
    if(!this.payload.expiresAt){
      this.message.add({
        severity:"error",
        summary:"Data de expiração não informada"
      })
      return;
    }
    this.loadingSave = true;
    this.descontoService.atualizarCadastrarDesconto(this.payload).subscribe({
      next:()=>{
        this.loadingSave = false;
        this.message.add({
          severity:"success",
          summary:"Sucesso"
        });
        this.open = false;
      },
      error:(err:HttpErrorResponse)=>{
        this.loadingSave = false;
        this.message.add({
          severity:"error",
          summary:err.error
        })
      }
    })
  }



}
