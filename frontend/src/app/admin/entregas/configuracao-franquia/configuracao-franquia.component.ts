import { Component, OnInit } from '@angular/core';
import {CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../services/correios-franquias-context.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../../services/user.service';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-configuracao-franquia',
  imports: [SharedModule],
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
          this.loadingConfigs = false;
        }else{
          this.initializePayload();
          this.loadingConfigs = false;
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
      franquiaId:this.auth.getContext()?.franquia.systemId as string,
      usuario:"",
      senha:"",
      codigoPac:"",
      codigoSedex:"",
      cepOrigem:"",
      numeroContrato:"",
      numeroDiretoriaRegional:""
    }
  }

  isValid(){
    if(!this.currentConfig.numeroContrato || !this.currentConfig.numeroDiretoriaRegional || !this.currentConfig.usuario || !this.currentConfig.senha || !this.currentConfig.codigoPac || !this.currentConfig.codigoSedex || !this.currentConfig.cepOrigem){
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
