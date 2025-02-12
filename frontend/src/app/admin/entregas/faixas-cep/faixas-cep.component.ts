import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../services/correios-franquias-context.service';
import { UserService } from '../../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CepPipePipe } from '../../../pipes/cep-pipe.pipe';
import { CriarFaixaComponent } from "./criar-faixa/criar-faixa.component";
import { EditarFaixaComponent } from "./editar-faixa/editar-faixa.component";

@Component({
  selector: 'app-faixas-cep',
  imports: [SharedModule, CepPipePipe, CriarFaixaComponent, EditarFaixaComponent],
  templateUrl: './faixas-cep.component.html'
})
export class FaixasCepComponent implements OnInit {

  faixas!:CorreiosFranquiasContext.FaixaCep[];
  loadingFaixas = false;

  constructor(
    private correiosFranquiaContext:CorreiosFranquiasContextService,
    private auth:UserService
  ){}

  ngOnInit(): void {
    this.loadingFaixas = true;
    this.correiosFranquiaContext.getFaixas(this.auth.getContext()?.franquia.systemId as string).subscribe({
      next:(data)=>{
        this.loadingFaixas = false;
        this.faixas = data;
      },
      error:(e:HttpErrorResponse)=>{
        alert(e.error);
        this.loadingFaixas = false;
      }
    })
  }

  forceType(row:any){
    return row as CorreiosFranquiasContext.FaixaCep;
  }

  onCadastro(novo:CorreiosFranquiasContext.FaixaCep){
    this.faixas.unshift(novo);
  }

  onUpdate(updated:CorreiosFranquiasContext.FaixaCep){
    this.faixas = this.faixas.map((e)=>{
      if(e.systemId===updated.systemId){
        return updated;
      }else{
        return e;
      }
    })
  }


}
