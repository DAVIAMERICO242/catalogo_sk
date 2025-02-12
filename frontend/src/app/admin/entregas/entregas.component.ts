import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { Router, RouterModule } from '@angular/router';
import { CorreiosFranquiasContextService } from '../../services/correios-franquias-context.service';
export enum EntregasPage{
  INTEGRACAO="INTEGRACAO",
  PESAGEM="PESAGEM",
  DIMENSOES="DIMENSOES",
  FAIXA_CEP="FAIXA_CEP"
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
    if(url.endsWith("/pesagem")){
      this.focusedPage = EntregasPage.PESAGEM;
    }
    if(url.endsWith("/dimensoes")){
      this.focusedPage = EntregasPage.DIMENSOES;
    }
    if(url.endsWith("/faixas-cep")){
      this.focusedPage = EntregasPage.FAIXA_CEP;
    }
  }

  navToSubRoute(page:EntregasPage){
    if(page===EntregasPage.INTEGRACAO){
      this.router.navigate(["admin/entregas/integracao-correios"]);
      this.focusedPage = page;
    }
    if(page===EntregasPage.PESAGEM){
      this.router.navigate(["admin/entregas/pesagem"]);
      this.focusedPage = page;
    }
    if(page===EntregasPage.DIMENSOES){
      this.router.navigate(["admin/entregas/dimensoes"]);
      this.focusedPage = page;
    }
    if(page===EntregasPage.FAIXA_CEP){
      this.router.navigate(["admin/entregas/faixas-cep"]);
      this.focusedPage = page;
    }
  }


}
