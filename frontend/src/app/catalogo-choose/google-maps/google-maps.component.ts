import { Component, Input, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { Loja } from '../../services/loja.service';

@Component({
  selector: 'app-google-maps',
  imports: [SharedModule],
  templateUrl: './google-maps.component.html'
})
export class GoogleMapsComponent implements OnInit {
  
  @Input({required:true})
  loja!:Loja.Loja;
  googleUrl = "";
  
  ngOnInit(): void {
    this.buildGoogleUrl();
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
