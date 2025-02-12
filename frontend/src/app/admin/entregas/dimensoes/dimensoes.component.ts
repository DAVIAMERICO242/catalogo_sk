import { Component, OnInit } from '@angular/core';
import { CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../services/correios-franquias-context.service';
import { UserService } from '../../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-dimensoes',
  imports: [SharedModule],
  templateUrl: './dimensoes.component.html'
})
export class DimensoesComponent implements OnInit {

  payload!:CorreiosFranquiasContext.DimensaoCaixa;
  loadingChange = false;

  constructor(
    private franquiaCorreiosContext:CorreiosFranquiasContextService,
    private auth:UserService,
    private message:MessageService
  ){}

  ngOnInit(): void {
    this.franquiaCorreiosContext.getDimensoesByFranquiaId(this.auth.getContext()?.franquia.systemId as string).subscribe({
      next:(data)=>{
        if(data){
          this.payload = data;
        }else{
          this.initializePayload();
        }
      },
      error:(e:HttpErrorResponse)=>{
        alert(e.error);
      }
    })
  }

  initializePayload(){
    this.payload = {
      systemId:"",
      franquiaId:this.auth.getContext()?.franquia.systemId as string,
      altura:0,
      comprimento:0,
      largura:0
    }
  }

  cadastrarAtualizar(){
    if(!this.payload.altura || !this.payload.largura || !this.payload.comprimento){
      this.message.add({
        severity:"error",
        summary:"Dados inválidos"
      })
      return;
    }
    this.loadingChange = true;
    this.franquiaCorreiosContext.cadastrarAtualizarDimensoes(this.payload).subscribe({
      next:(data)=>{
        this.payload = {...data};
        this.loadingChange = false;
        this.message.add({
          severity:"success",
          summary:"Sucesso"
        })
      },
      error:(e:HttpErrorResponse)=>{
        this.loadingChange = false;
        this.message.add({
          severity:"error",
          summary:e.error
        })
      }
    })
  }

}
