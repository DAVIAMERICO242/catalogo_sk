import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { Router, RouterModule } from '@angular/router';
export enum EntregasPage{
  INTEGRACAO="INTEGRACAO",
  PESAGEM_COMPRIMENTAGEM="PESAGEM_COMPRIMENTAGEM"
}
@Component({
  selector: 'app-entregas',
  imports: [SharedModule, AdminPageTitleComponent,RouterModule],
  templateUrl: './entregas.component.html'
})
export class EntregasComponent implements OnInit{
  focusedPage!:EntregasPage;
  EntregaesPage = EntregasPage;

  constructor(private router:Router){}
  ngOnInit(): void {
    const url = window.location.href;
    if(url.endsWith("/integracao-correios")){
      this.focusedPage = EntregasPage.INTEGRACAO
    }
    if(url.endsWith("/pesagem-comprimentagem")){
      this.focusedPage = EntregasPage.PESAGEM_COMPRIMENTAGEM;
    }
  }

  navToSubRoute(page:EntregasPage){
    if(page===EntregasPage.INTEGRACAO){
      this.router.navigate(["admin/entregas/integracao-correios"]);
      this.focusedPage = page;
    }
    if(page===EntregasPage.PESAGEM_COMPRIMENTAGEM){
      this.router.navigate(["admin/entregas/pesagem-comprimentagem"]);
      this.focusedPage = page;
    }
  }


}
