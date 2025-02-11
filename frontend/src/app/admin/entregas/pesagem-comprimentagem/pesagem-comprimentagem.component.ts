import { Component, OnInit } from '@angular/core';
import { ProdutosService } from '../../../services/produtos.service';
import { SharedModule } from '../../../shared/shared.module';
import { UserService } from '../../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../services/correios-franquias-context.service';
import { NovoPesoComponent } from "./novo-peso/novo-peso.component";
import { EditarPesoComponent } from "./editar-peso/editar-peso.component";

@Component({
  selector: 'app-pesagem-comprimentagem',
  imports: [SharedModule, NovoPesoComponent, EditarPesoComponent],
  templateUrl: './pesagem-comprimentagem.component.html'
})
export class PesagemComprimentagemComponent implements OnInit {

  categorias!:string[]
  pesosCategorias!:CorreiosFranquiasContext.PesoCategoria[];

  constructor(
    private produtoService:ProdutosService,
    private auth:UserService,
    private correiosFranquiasService:CorreiosFranquiasContextService
  ){}
  ngOnInit(): void {
    this.produtoService.getTermos(this.auth.getContext()?.franquia.systemId as string).subscribe({
      next:(data)=>{
        this.categorias = data.categorias.sort((a,b)=>a.localeCompare(b));
      },
      error:(e:HttpErrorResponse)=>{
        alert(e.error);
      }
    });
    this.correiosFranquiasService.getPesoCategorias(this.auth.getContext()?.franquia.systemId as string).subscribe({
      next:(data)=>{
        this.pesosCategorias = data;
      },
      error:(e:HttpErrorResponse)=>{
        alert(e.error);
      }
    })
  }

  onCadastro(x:CorreiosFranquiasContext.PesoCategoria){
    this.pesosCategorias.unshift({...x});
  }

  onAtualizar(x:CorreiosFranquiasContext.PesoCategoria){
    this.pesosCategorias = [...this.pesosCategorias.map((e)=>{
      if(e.systemId===x.systemId){
        return x
      }else{
        return e;
      }
    })]
  }

  forceType(row:any){
    return row as CorreiosFranquiasContext.PesoCategoria;
  }

}
