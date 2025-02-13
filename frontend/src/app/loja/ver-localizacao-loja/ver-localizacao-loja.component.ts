import { Component, Input } from '@angular/core';
import { Loja } from '../../services/loja.service';
import { LojaContextService } from '../loja-context.service';
import { SharedModule } from '../../shared/shared.module';

@Component({
  selector: 'app-ver-localizacao-loja',
  imports: [SharedModule],
  templateUrl: './ver-localizacao-loja.component.html'
})
export class VerLocalizacaoLojaComponent {
  open = false;
  @Input()
  loja!:Loja.Loja;
  @Input()
  styleClass = "";
  googleUrl = "";


  constructor(private lojaContext:LojaContextService){
    if(!this.loja){
      this.lojaContext.loja$.subscribe((loja)=>{
        if(loja){
          this.loja = loja;
          this.buildGoogleUrl();
        }
      })
    }
  }

  buildGoogleUrl(){
    this.googleUrl = "https://www.google.com/maps?q="+this.loja.endereco?.replaceAll(" ","+")+"+"+this.loja.loja.replaceAll(" ","+");
  }

  abrirGoogleMaps(){
    if(this.googleUrl){
      window.open(this.googleUrl, '_blank');
    }
  }
}
