import { Component, Input } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { Desconto } from '../../../services/descontos.service';
import { Pedidos } from '../../../services/pedidos.service';

@Component({
  selector: 'app-detalhar-desconto',
  imports: [SharedModule],
  templateUrl: './detalhar-desconto.component.html'
})
export class DetalharDescontoComponent {
  @Input({required:true})
  descontosAplicados:Pedidos.DescontoAplicado[] = [];
  open = false;

  getTotalDescontos(val:Pedidos.DescontoAplicado[]){
    return val.reduce((a,b)=>a+b.valorAplicado,0)
  }

  forceType(val:any){
    return val as Pedidos.DescontoAplicado;
  }
}
