import { Component, Input } from '@angular/core';
import { Pedidos } from '../../../services/pedidos.service';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-status-pedido',
  imports: [SharedModule],
  templateUrl: './status-pedido.component.html'
})
export class StatusPedidoComponent {
  @Input({required:true})
  pedido!:Pedidos.Pedido;

}
