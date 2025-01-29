import { Component, Input, OnInit } from '@angular/core';
import { Pedidos, PedidosService } from '../../../services/pedidos.service';
import { SharedModule } from '../../../shared/shared.module';
import { DocumentoPipe } from '../../../pipes/documento.pipe';
import { PhonePipePipe } from '../../../pipes/phone-pipe.pipe';
import { CepPipePipe } from '../../../pipes/cep-pipe.pipe';


@Component({
  selector: 'app-pedido-view',
  imports: [SharedModule,DocumentoPipe,PhonePipePipe,CepPipePipe],
  templateUrl: './pedido-view.component.html'
})
export class PedidoViewComponent implements OnInit {
  @Input({required:true})
  pedido!:Pedidos.Pedido;
  pedidoVariacoesReduced!:Pedidos.PedidoReducedTypes.VariacaoPedidoReduced[];
  open = false;

  constructor(private pedidoService:PedidosService) {
    
  }
  
  
  ngOnInit(): void {
    this.pedidoVariacoesReduced = this.pedidoService.reducePedido(this.pedido).produtos.flatMap(e => e.variacoesCompradas);
    console.log(this.pedidoVariacoesReduced);
  }

  getBeautyAddress(row:Pedidos.Pedido){
    return  row.numero + " ," + "    Rua: " + row.rua + ",   " + "Bairro: " + row.bairro + " ," + "Cidade: " + row.cidade + "/" + row.estado;
  }

  asReducedVariation(val:any){
    return val as Pedidos.PedidoReducedTypes.VariacaoPedidoReduced;
  }

  getParentNameOfVariationId(variationId:string){
    const parentName = this.pedido.produtos.find((e)=>e.variacoesCompradas.map(e1=>e1.systemId).includes(variationId))?.nome || "";
    console.log(parentName)
    return parentName;
  }

  getValorBruto(){
    return this.pedido.produtos.reduce((a,b)=>a+b.valorBase,0)
  }

  getTotalDescontos(){
    return this.pedido.descontosAplicados.reduce((a,b)=>a+b.valorAplicado,0)
  }
  
}
