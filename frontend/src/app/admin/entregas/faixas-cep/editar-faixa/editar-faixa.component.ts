import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../../services/correios-franquias-context.service';
import { UserService } from '../../../../services/user.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedModule } from '../../../../shared/shared.module';

@Component({
  selector: 'app-editar-faixa',
  imports: [SharedModule],
  templateUrl: './editar-faixa.component.html'
})
export class EditarFaixaComponent implements OnInit {

  @Input({required:true})
  payload!:CorreiosFranquiasContext.FaixaCep;
  loading = false;
  open = false;
  @Output()
  onUpdate = new EventEmitter<typeof this.payload>();

  constructor(
    private auth:UserService,
    private correiosFranquiasContext:CorreiosFranquiasContextService ,
    private message:MessageService
  ){}
  ngOnInit(): void {
    this.payload = {...this.payload};
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
        this.onUpdate.emit(data);
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
