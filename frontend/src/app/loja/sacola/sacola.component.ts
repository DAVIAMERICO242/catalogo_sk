import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { Sacola, SacolaService } from '../../services/sacola.service';

@Component({
  selector: 'app-sacola',
  imports: [SharedModule],
  templateUrl: './sacola.component.html'
})
export class SacolaComponent implements OnInit {
  beautySacola!:Sacola.BeautySacola|undefined;
  constructor(private sacolaService:SacolaService){}
  ngOnInit(): void {
    
  }

}
