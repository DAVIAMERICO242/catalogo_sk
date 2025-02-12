import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../../services/correios-franquias-context.service';
import { UserService } from '../../../../services/user.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-criar-faixa',
  imports: [SharedModule],
  templateUrl: './criar-faixa.component.html'
})
export class CriarFaixaComponent implements OnInit {
  payload!:CorreiosFranquiasContext.FaixaCep;
  loading = false;
  open = false;

  constructor(
    private auth:UserService,
    private correiosFranquiasContext:CorreiosFranquiasContextService,
    private message:MessageService
  ){}
  ngOnInit(): void {
    this.initializePayload();
  }

  initializePayload(){
    this.payload = {
      cepFim:"",
      cepInicio:"",
      franquiaId:this.auth.getContext()?.franquia.systemId as string,
      minValueToApply:0,
      systemId:"",
      valorFixo:0,
      prazo:3
    }
  }

  cadastrarAtualizar(){
    if(this.payload.cepFim.length!==8 && this.payload.cepInicio.length!==8){
      this.message.add({
        severity:"error",
        summary:"CEP inválido"
      })
      return;
    }
    this.loading = true;
    this.correiosFranquiasContext.cadastrarAtualizarFaixa(this.payload).subscribe({
      next:(data)=>{
        this.payload = data;
        this.loading = false;
        this.open = false;
      },
      error:(e:HttpErrorResponse)=>{
        this.message.add({
          severity:"error",
          summary:e.error
        })
      }
    })
  }



}
