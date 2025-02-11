import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Pedidos, PedidosService } from '../../../services/pedidos.service';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedModule } from '../../../shared/shared.module';
import { ConfirmationDialogComponent } from "../../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-deletar-pedido',
  imports: [SharedModule, ConfirmationDialogComponent],
  templateUrl: './deletar-pedido.component.html'
})
export class DeletarPedidoComponent {
  @Input({required:true})
  pedido!:Pedidos.Pedido;
  loadingDelete = false;
  open = false;
  @Output()
  onDelete = new EventEmitter<string>();

  constructor(private pedidoService:PedidosService){}


  deletarPedido(){
    this.loadingDelete = true;
    this.pedidoService.deletarPedido(this.pedido.systemId).subscribe({
      next:()=>{
        this.loadingDelete = false;
        this.onDelete.emit(this.pedido.systemId);
      },
      error:(err:HttpErrorResponse)=>{
        alert(err.error);
      }
    })
  }



}
