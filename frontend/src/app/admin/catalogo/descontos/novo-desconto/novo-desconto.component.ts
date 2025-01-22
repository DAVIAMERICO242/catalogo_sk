import { Component, Input, OnInit } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { Desconto, DescontosService } from '../../../../services/descontos.service';
import { DescontosBeautyNomes } from '../descontos.component';
import { UserService } from '../../../../services/user.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-novo-desconto',
  imports: [SharedModule],
  templateUrl: './novo-desconto.component.html'
})
export class NovoDescontoComponent implements OnInit {
  open = false;
  @Input({required:true})
  descontoTipos:DescontosBeautyNomes[] = [];
  payload!:Desconto.DescontoModel;
  focusedTipo!:DescontosBeautyNomes;
  loadingSave = false;

  constructor(private auth:UserService,private message:MessageService,private descontoService:DescontosService) {
    
  }
  
  ngOnInit(): void {
    this.focusedTipo = this.descontoTipos[0];
    this.definePayLoadModel();
  }

  definePayLoadModel(){
    const contextualLoja = this.auth.getContext()?.loja;
    const loja:Desconto.Loja = {
      nome:contextualLoja?.nome as string,
      slug:contextualLoja?.slug  as string,
      systemId:contextualLoja?.systemId  as string
    }
    this.payload = {
      systemId: '',
      loja: loja,
      discountName: '',
      descriptionDelimitation: '',
      isActive: false,
      expiresAt: new Date(),
      cartRequiredQuantity: 1,
      totalCartValueDiscount:0
    };
  }

  manageTipoChange(){
    this.definePayLoadModel();
  }

  get totalCartDecimalPercentDiscountView(): number {
    if(this.payload.totalCartDecimalPercentDiscount){
      return this.payload.totalCartDecimalPercentDiscount*100;
    }else{
      return 0;
    }
  }

  set totalCartDecimalPercentDiscountView(val:number){
    this.payload.totalCartDecimalPercentDiscount = val / 100;
  }

  testar(){
    alert(this.payload.totalCartDecimalPercentDiscount);
  }

  submit(){
    if(!this.payload.discountName){
      this.message.add({
        severity:"error",
        summary:"O desconto precisa de um nome"
      })
      return;
    }
    switch(this.focusedTipo.pure_name){
        case Desconto.DescontoTipo.DESCONTO_TOTAL_CARRINHO:
          if(!this.payload.totalCartValueDiscount && !this.payload.totalCartDecimalPercentDiscount){
            this.message.add({
              severity:"error",
              summary:"O valor do desconto é 0"
            })
            return;
          }

    }
    this.loadingSave = true;
    this.descontoService.atualizarCadastrarNivelLoja(this.payload).subscribe({
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
