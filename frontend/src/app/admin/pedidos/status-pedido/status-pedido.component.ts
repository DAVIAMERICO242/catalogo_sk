import { Component, Input } from '@angular/core';
import { Pedidos, PedidosService } from '../../../services/pedidos.service';
import { SharedModule } from '../../../shared/shared.module';
import { MessageService } from 'primeng/api';
import { ConfirmationDialogComponent } from "../../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-status-pedido',
  imports: [SharedModule, ConfirmationDialogComponent],
  templateUrl: './status-pedido.component.html'
})
export class StatusPedidoComponent {
  @Input({required:true})
  pedido!:Pedidos.Pedido;
  loadingAlterar = false;
  openAlterar = false;
  constructor(private pedidoService:PedidosService,private message:MessageService){}

  alterar(){
    this.loadingAlterar = true;
    this.pedidoService.alterarStatus(this.pedido.systemId).subscribe({
      next:()=>{
        this.pedido.pago = !this.pedido.pago;
        this.loadingAlterar = false;
        this.openAlterar = false;
      },
      error:()=>{
        alert("Erro")
      }
    })
  }
}
