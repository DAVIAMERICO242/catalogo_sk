import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../../services/correios-franquias-context.service';
import { UserService } from '../../../../services/user.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedModule } from '../../../../shared/shared.module';

@Component({
  selector: 'app-editar-peso',
  imports: [SharedModule],
  templateUrl: './editar-peso.component.html'
})
export class EditarPesoComponent implements OnInit {
  open = false;

  @Input({required:true})
  categorias!:string[];
  @Input({required:true})
  payload!:CorreiosFranquiasContext.PesoCategoria;
  loadingSave = false;
  @Output()
  onUpdate = new EventEmitter<typeof this.payload>();

  constructor(private morteService:CorreiosFranquiasContextService,private auth:UserService,private message:MessageService){}
  ngOnInit(): void {
    this.payload = {...this.payload};
  }

  atualizar(){
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
        this.onUpdate.emit(this.payload)
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
