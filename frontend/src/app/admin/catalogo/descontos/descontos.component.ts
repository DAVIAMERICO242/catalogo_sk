import { Component, OnInit } from '@angular/core';
import { Desconto, DescontosService } from '../../../services/descontos.service';
import { UserService } from '../../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedModule } from '../../../shared/shared.module';
import { NovoDescontoComponent } from "./novo-desconto/novo-desconto.component";

export interface DescontosBeautyNomes{
  pure_name:Desconto.DescontoTipo,
  beauty_name:string
}

@Component({
  selector: 'app-descontos',
  imports: [SharedModule, NovoDescontoComponent],
  templateUrl: './descontos.component.html'
})
export class DescontosComponent implements OnInit {

  descontos!:Desconto.DescontoModel[];
  descontosTipos:DescontosBeautyNomes[] = [];
  loadingDescontos = false;
  open = false;
  
  constructor(private descontoService:DescontosService,private auth:UserService){}

  ngOnInit(): void {
    for(let key in Desconto.DescontoTipo){
      const pure_name  = key;
      let beauty_name = "";
      switch (pure_name){
        case Desconto.DescontoTipo.DESCONTO_TOTAL_CARRINHO:
          beauty_name = "Desconto total carrinho"
          break;
        case Desconto.DescontoTipo.DESCONTO_TERMO:
          beauty_name = "Desconto baseado no termo do produto"
          break;
        case Desconto.DescontoTipo.DESCONTO_PECA_MAIOR_VALOR:
          beauty_name = "Desconto peça maior valor"
          break;
        case Desconto.DescontoTipo.DESCONTO_PECA_MENOR_VALOR:
          beauty_name = "Desconto peça menor valor"
          break;
        case Desconto.DescontoTipo.COMPRE_X_GANHE_1:
          beauty_name = "Compre X e ganhe uma peça de presente"
          break;
      }
      this.descontosTipos.push({
        beauty_name:beauty_name,
        pure_name:pure_name as Desconto.DescontoTipo
      })
    }
    this.getAll();
  }

  getAll(){
    const lojaId = this.auth.getContext()?.loja.systemId;
    if(lojaId){
      this.loadingDescontos = true;
      this.descontoService.getAllNivelLoja(lojaId).subscribe({
        next:(data)=>{
          this.descontos = data;
          this.loadingDescontos = false;
        },error:(error:HttpErrorResponse)=>{
          this.loadingDescontos = false;
          alert(error.error);
        }
      })
    }
  }

}
