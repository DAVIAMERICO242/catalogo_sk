import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../../services/correios-franquias-context.service';
import { UserService } from '../../../../services/user.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { OutletContext } from '@angular/router';

@Component({
  selector: 'app-novo-peso',
  imports: [SharedModule],
  templateUrl: './novo-peso.component.html'
})
export class NovoPesoComponent implements OnInit {

  open = false;

  @Input({required:true})
  categorias!:string[];
  payload!:CorreiosFranquiasContext.PesoCategoria;
  loadingSave = false;
  @Output()
  onCadastro = new EventEmitter<typeof this.payload>();


  constructor(private morteService:CorreiosFranquiasContextService,private auth:UserService,private message:MessageService){}


  ngOnInit(): void {
    this.initializePayload();
  }

  initializePayload(){
    this.payload={
      categoria:"",
      franquiaId:this.auth.getContext()?.franquia.systemId as string,
      pesoGramas:300,
      systemId:""
    }
  }

  cadastrar(){
    this.loadingSave = true;
    if(!this.payload.categoria && !this.payload.pesoGramas){
      alert("Dados inválidos");
      return;
    }
    this.morteService.cadastrarAtualizarPeso(this.payload).subscribe({
      next:(data)=>{
        this.payload = data;
        this.loadingSave = false;
        this.open = false;
        this.onCadastro.emit(this.payload)
      },
      error:(e:HttpErrorResponse)=>{
        this.loadingSave = false;
        this.message.add({
          severity:"error",
          summary:e.error
        })
      }
    })
  }



}
