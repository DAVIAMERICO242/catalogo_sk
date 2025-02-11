import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { Pedidos, PedidosService } from '../../services/pedidos.service';
import { UserService } from '../../services/user.service';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { DatetimeBrazilPipe } from "../../pipes/datetime-brazil.pipe";
import { DateBrazilPipe } from '../../pipes/date-brazil.pipe';
import { PedidoViewComponent } from "./pedido-view/pedido-view.component";
import { DetalharDescontoComponent } from "./detalhar-desconto/detalhar-desconto.component";
import { StatusPedidoComponent } from "./status-pedido/status-pedido.component";
import { DeletarPedidoComponent } from "./deletar-pedido/deletar-pedido.component";

@Component({
  selector: 'app-pedidos',
  imports: [SharedModule, AdminPageTitleComponent, DatetimeBrazilPipe, PedidoViewComponent, DetalharDescontoComponent, StatusPedidoComponent, DeletarPedidoComponent],
  templateUrl: './pedidos.component.html'
})
export class PedidosComponent implements OnInit{

  pedidos!:Pedidos.Pedido[]
  loading = false;

  constructor(
    private pedidoService:PedidosService,
    private auth:UserService,
    private message:MessageService
  ){}

  ngOnInit(): void {
    this.loadPedidos();
  }

  loadPedidos(){
    const franquiaId = this.auth.getContext()?.franquia.systemId;
    if(franquiaId){
      const lojaId = this.auth.getContext()?.loja?.systemId;//se for gestor e nulo e retornara todas lojas da franquia
      this.loading = true;
      this.pedidoService.getPedidos(franquiaId,lojaId).subscribe({
        next:(data)=>{
          this.pedidos = data;
          this.loading = false
        },
        error:(err:HttpErrorResponse)=>{
          this.loading = false
          this.message.add({
            severity:"error",
            summary:err.error
          })
        }
      })
    }
    
  }

  onDelete(id:string){
    this.pedidos = this.pedidos.filter((e)=>e.systemId!==id);
    this.message.add({
      severity:"success",
      summary:"Pedido deletado com sucesso"
    })
  }

  forceType(val:any){
    return val as Pedidos.Pedido;
  }

  getBeautyAddress(row:Pedidos.Pedido){
    return  row.numero + " ," + "    Rua: " + row.rua + ",   " + "Bairro: " + row.bairro + " ," + "Cidade: " + row.cidade + "/" + row.estado;
  }

  getTotalDescontos(val:Pedidos.DescontoAplicado[]){
    return val.reduce((a,b)=>a+b.valorAplicado,0)
  }

}
