import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DescontosService } from '../../../../services/descontos.service';
import { ConfirmationDialogComponent } from "../../../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-deletar-desconto',
  imports: [ConfirmationDialogComponent],
  templateUrl: './deletar-desconto.component.html'
})
export class DeletarDescontoComponent {

  @Input({required:true})
  id!:string;
  @Output()
  onDelete = new EventEmitter<string>();
  loading = false;
  open = false;

  constructor(private descontoService:DescontosService,private message:MessageService){

  }

  deletar(){
    this.loading = true;
    this.descontoService.delete(this.id).subscribe({
      next:()=>{
        this.loading = false;
        this.open = false
        this.onDelete.emit(this.id);
        this.message.add({
          severity:"success",
          summary:"Sucesso"
        })
      },
      error:(e:HttpErrorResponse)=>{
        this.loading = false;
        this.message.add({
          severity:"error",
          summary:e.error
        })
      }
    })
  }

}
