import { Component, OnInit } from '@angular/core';
import {CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../services/correios-franquias-context.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-configuracao-franquia',
  imports: [],
  templateUrl: './configuracao-franquia.component.html'
})
export class ConfiguracaoFranquiaComponent implements OnInit {
  currentConfig!:CorreiosFranquiasContext.CorreiosFranquias;
  loadingSave = false;
  loadingConfigs = false;

  constructor(
    private correiosContextService:CorreiosFranquiasContextService,
    private message:MessageService,
    private auth:UserService
  ){}
  ngOnInit(): void {
    this.loadingConfigs = true;
    this.correiosContextService.getByFranquiaId(this.auth.getContext()?.franquia.systemId as string).subscribe({
      next:(data)=>{
        if(data){
          this.currentConfig = data;
        }else{
          this.initializePayload();
        }
      },
      error:(err:HttpErrorResponse)=>{
        alert(err.error);
      }
    });
  }

  initializePayload(){
    this.currentConfig = {
      systemId:"",
      franquiaId:"",
      usuario:"",
      senha:"",
      codigoPac:0,
      codigoSedex:0,
      cepOrigem:"",
    }
  }

  isValid(){
    if(!this.currentConfig.usuario || !this.currentConfig.senha || !this.currentConfig.codigoPac || !this.currentConfig.codigoSedex || !this.currentConfig.cepOrigem){
      this.message.add({
        severity:"error",
        summary:"Dados inválidos"
      })
      return false;
    }

    if(this.currentConfig.cepOrigem.length!==8){
      this.message.add({
        severity:"error",
        summary:"CEP inválido"
      })
      return false;
    }
    return true;
  }

  saveConfigs(){
    if(!this.isValid()){
      return;
    }
    this.loadingSave = true;
    this.correiosContextService.criarAtualizar(this.currentConfig).subscribe({
      next:(data)=>{
        this.message.add({
          severity:"success",
          summary:"Atualizado com sucesso"
        });
        this.currentConfig.systemId = data.systemId;
        this.loadingSave = false;
      },
      error:(err:HttpErrorResponse)=>{
        this.loadingSave = false;
        alert(err.error);
      }
    })
  }

}
