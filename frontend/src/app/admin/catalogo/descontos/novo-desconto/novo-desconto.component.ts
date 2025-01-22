import { Component, Input, OnInit } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { Desconto } from '../../../../services/descontos.service';
import { DescontosBeautyNomes } from '../descontos.component';
import { UserService } from '../../../../services/user.service';

@Component({
  selector: 'app-novo-desconto',
  imports: [SharedModule],
  templateUrl: './novo-desconto.component.html'
})
export class NovoDescontoComponent implements OnInit {
  open = false;
  @Input({required:true})
  descontoTipos:DescontosBeautyNomes[] = []
  payload!:Desconto.DescontoModel;
  focusedTipo!:DescontosBeautyNomes

  constructor(private auth:UserService) {
    
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
}
