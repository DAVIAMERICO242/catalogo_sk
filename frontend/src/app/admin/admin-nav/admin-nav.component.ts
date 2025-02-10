import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { User, UserService } from '../../services/user.service';
import { Router } from '@angular/router';
enum Page{
  PRODUTOS="PRODUTOS",
  PEDIDOS="PEDIDOS",
  CATALOGO="CATALOGO",
  BANNERS="BANNERS",
  LOJAS="LOJAS"
}
@Component({
  selector: 'app-admin-nav',
  imports: [SharedModule],
  templateUrl: './admin-nav.component.html'
})
export class AdminNavComponent implements OnInit {
  PageEnum = Page;
  @Input({required:true})
  expanded:boolean = false;
  @Output()
  expandedChange = new EventEmitter<boolean>()
  beautyName=""
  focused = Page.PRODUTOS

  constructor(protected auth:UserService, private router:Router){

  }
  ngOnInit(): void {
    const name = this.auth.getContext()?.beautyName
    this.beautyName = name || ""
    const endPath = window.location.href;
    if(endPath.endsWith("/pedidos")){
      this.focused = Page.PEDIDOS
    }
    if(endPath.endsWith("/produtos")){
      this.focused = Page.PRODUTOS
    }
    if(endPath.endsWith("/catalogo")){
      this.focused = Page.CATALOGO
    }
    if(endPath.endsWith("/banners")){
      this.focused = Page.BANNERS
    }
    if(endPath.endsWith("/lojas")){
      this.focused = Page.LOJAS
    }
  }

  changePage(page:Page,route:string){
    this.focused = page;
    this.changeAdminRoute(route);
  }

  changeAdminRoute(route:string){
    this.router.navigate(["/admin"+route])
  }

  emitSpanChange(){
    this.expanded = !this.expanded;
    this.expandedChange.emit(this.expanded)
  }

  navigateToLoja(){
    if(this.auth.getContext()?.role === User.Role.OPERACIONAL){
      window.open("/" + (this.auth.getContext()?.loja.slug),"_blank")
    }else{
      if(this.auth.getContext()?.franquia.isMatriz){
        window.open("/matriz","_blank")
      }else{
        window.open("/franquia","_blank")
      }
    }
  }

}
