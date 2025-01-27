import { Component } from '@angular/core';
import { RouteConfigLoadEnd, RouteConfigLoadStart, Router, RouterOutlet } from '@angular/router';
import { PrimeNG } from 'primeng/config';
import { MyTheme } from './app.config';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SharedModule } from './shared/shared.module';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,ToastModule,SharedModule],
  templateUrl: './app.component.html',
  providers:[MessageService]
})
export class AppComponent {
  title = 'catalogo-sk';

  loadingLazyComponent!: boolean;

  constructor(private primeng: PrimeNG,private router: Router){

  }
  ngOnInit(): void {
    this.setTheme();
    this.setApplicationLanguage();
    this.router.events.subscribe(event => {
      if (event instanceof RouteConfigLoadStart) {
          this.loadingLazyComponent = true;
      } else if (event instanceof RouteConfigLoadEnd) {
          this.loadingLazyComponent = false;
      }
  });
  }

  setTheme(){
    this.primeng.theme.set({
      preset: MyTheme
    });
  }

  setApplicationLanguage(){
    const monthNames = ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'];
    const monthNamesShort = monthNames.map((e)=>e.slice(0,3));
    const dayNames = ['Segunda','Terça','Quarta','Quinta','Sexta','Sábado','Doming'];
    const dayNamesShort = dayNames.map(e=>e.slice(0,3))
    const dayNamesMin = dayNamesShort;
    this.primeng.setTranslation({
      monthNames:monthNames,
      monthNamesShort: monthNamesShort,
      dayNames:dayNames,
      dayNamesShort:dayNamesShort,
      dayNamesMin:dayNamesMin
    })
  }
}
