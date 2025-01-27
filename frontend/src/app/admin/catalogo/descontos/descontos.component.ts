import { Component, OnInit } from '@angular/core';
import { Desconto, DescontosService } from '../../../services/descontos.service';
import { UserService } from '../../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedModule } from '../../../shared/shared.module';
import { DatetimeBrazilPipe } from "../../../pipes/datetime-brazil.pipe";
import { DateBrazilPipe } from '../../../pipes/date-brazil.pipe';
import { CriarAtualizarDescontoComponent } from './criar-atualizar-desconto/criar-atualizar-desconto.component';
import { DeletarDescontoComponent } from "./deletar-desconto/deletar-desconto.component";

export interface DescontosBeautyNomes{
  pure_name:Desconto.DescontoTipo,
  beauty_name:string
}

@Component({
  selector: 'app-descontos',
  imports: [SharedModule, DatetimeBrazilPipe, DateBrazilPipe, CriarAtualizarDescontoComponent, DeletarDescontoComponent],
  templateUrl: './descontos.component.html'
})
export class DescontosComponent implements OnInit {

  descontos!:Desconto.DescontoModel[];
  descontosTipos:DescontosBeautyNomes[] = [];
  loadingDescontos = false;
  open = false;
  
  constructor(private descontoService:DescontosService,private auth:UserService){
  }

  ngOnInit(): void {
    for(let key in Desconto.DescontoTipo){
      const pure_name  = key;
      let beauty_name = "";
      switch (pure_name){
        case Desconto.DescontoTipo.DESCONTO_FRETE:
          beauty_name = "Desconto frete"
          break;
        case Desconto.DescontoTipo.DESCONTO_GENERICO_CARRINHO:
          beauty_name = "Desconto carrinho a partir de um valor X"
          break;
        case Desconto.DescontoTipo.DESCONTO_SIMPLES_PRODUTO:
          beauty_name = "Desconto simples produto"
          break;
        case Desconto.DescontoTipo.DESCONTO_SIMPLES_TERMO:
          beauty_name = "Desconto em uma categoria, linha, grupo de produto"
          break;
        case Desconto.DescontoTipo.DESCONTO_PECA_MAIOR_VALOR:
          beauty_name = "Desconto peça de maior valor no carrinho"
          break;
        case Desconto.DescontoTipo.DESCONTO_PECA_MENOR_VALOR:
          beauty_name = "Desconto peça de menor valor no carrinho"
          break;
        case Desconto.DescontoTipo.DESCONTO_PROGRESSIVO:
          beauty_name = "Desconto progressivo"
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
      this.descontoService.getDescontos(lojaId).subscribe({
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

  forceType(val:any){
    return val as Desconto.DescontoModel;
  }

  getBeautyName(val:Desconto.DescontoTipo){
    return this.descontosTipos.find((e)=>e.pure_name===val)?.beauty_name;

  }

  onDescontoUpdate(val:Desconto.DescontoModel){
    const isPresent = this.descontos.find(e=>e.systemId===val.systemId)
    if(isPresent){
      this.descontos = this.descontos.map((e)=>{
        if(e.systemId===val.systemId){
          return val  
        }else{
          return e;
        }
      })
    }else{
      this.descontos.unshift(val);
    }
  }

  onDescontoDelete(val:string){
    this.descontos = this.descontos.filter((e)=>e.systemId!==val);
  }

}
