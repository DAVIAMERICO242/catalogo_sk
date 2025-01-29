import { Component, Input } from '@angular/core';
import { Pedidos } from '../../../services/pedidos.service';

@Component({
  selector: 'app-pedido-view',
  imports: [],
  templateUrl: './pedido-view.component.html'
})
export class PedidoViewComponent {
  @Input({required:true})
  pedido!:Pedidos.Pedido;
  
}
