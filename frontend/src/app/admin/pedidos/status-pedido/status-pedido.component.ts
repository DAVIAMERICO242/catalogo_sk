import { Component, Input } from '@angular/core';
import { Pedidos, PedidosService } from '../../../services/pedidos.service';
import { SharedModule } from '../../../shared/shared.module';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-status-pedido',
  imports: [SharedModule],
  templateUrl: './status-pedido.component.html'
})
export class StatusPedidoComponent {
  @Input({required:true})
  pedido!:Pedidos.Pedido;
  constructor(private pedidoService:PedidosService,private message:MessageService){}


}
