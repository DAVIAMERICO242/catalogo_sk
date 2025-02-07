import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { Desconto, DescontosService } from '../../../../services/descontos.service';
import { DescontosBeautyNomes } from '../descontos.component';
import { User, UserService } from '../../../../services/user.service';
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
  selector: 'app-criar-atualizar-desconto',
  imports: [SharedModule, DescontoFreteFormComponent, DescontoGenericoCarrinhoFormComponent, DescontoSimplesProdutoFormComponent, DescontoSimplesTermoFormComponent, DescontoMaiorValorFormComponent, DescontoMenorValorFormComponent, DescontoProgressivoFormComponent],
  templateUrl: './criar-atualizar-desconto.component.html'
})
export class CriarAtualizarDescontoComponent implements OnInit {
  open = false;
  @Input({required:true})
  descontoTipos:DescontosBeautyNomes[] = [];
  @Input()
  payload!:Desconto.DescontoModel;
  @Output()
  onSave = new EventEmitter<Desconto.DescontoModel>();
  @Input({required:true})
  update = false;
  focusedTipo!:DescontosBeautyNomes;
  loadingSave = false;
  nowAfter = new Date();
  DescontoTipoEnum = Desconto.DescontoTipo;


  constructor(protected auth:UserService,private message:MessageService,private descontoService:DescontosService) {
    
  }
  
  ngOnInit(): void {
    this.nowAfter.setDate(this.nowAfter.getDate()+1);
    this.nowAfter.setHours(0, 0, 0, 0);  // Set the time to 00:00:00.000
    if(!this.update){
      this.focusedTipo = this.descontoTipos[0];
      this.definePayLoadModel();
    }else{
      this.focusedTipo = this.descontoTipos.find((e)=>e.pure_name===this.payload.tipo) as DescontosBeautyNomes;
      this.payload = {...this.payload}
    }
  }

  definePayLoadModel(){
    this.payload = {
      ...this.payload,
      nome:"",
      tipo:this.focusedTipo.pure_name,
      systemId: this.payload?.systemId || "",
      lojas: [],
      isActive: true
    };

    if(this.auth.getContext()?.role === User.Role.ADMIN){
      const lojas = this.auth.getContext()?.lojasFranquia;
      lojas?.forEach((e)=>{
        this.payload.lojas.push({
          nome:e.nome,
          slug:e.slug,
          systemId:e.systemId
        })
      })
    }else{
      const loja = this.auth.getContext()?.loja;
      if(loja){
        this.payload.lojas.push({
          nome:loja.nome,
          slug:loja.slug,
          systemId:loja.systemId
        })
      }
    } 
  }

  resetFormIfCadastro(){
    if(!this.update){
      this.definePayLoadModel()
    }
  }

  manageTipoChange(){
    this.definePayLoadModel();
  }

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
    if(!this.payload.lojas.length){
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
      next:(data)=>{
        this.payload = data;
        this.onSave.emit(data);
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
