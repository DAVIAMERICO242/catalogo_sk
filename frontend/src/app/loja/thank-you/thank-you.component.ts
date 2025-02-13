import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Pedidos } from '../../services/pedidos.service';

@Component({
  selector: 'app-thank-you',
  imports: [],
  templateUrl: './thank-you.component.html'
})
export class ThankYouComponent {
  pedidoReduced!:Pedidos.PedidoReducedTypes.PedidoReduced;
  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state) {
      this.pedidoReduced =  navigation?.extras.state as Pedidos.PedidoReducedTypes.PedidoReduced;
    }
  }

}
