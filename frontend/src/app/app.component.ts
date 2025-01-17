import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PrimeNG } from 'primeng/config';
import { MyTheme } from './app.config';
import { MessageService } from 'primeng/api';
import { SharedModule } from './shared/shared.module';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,SharedModule],
  templateUrl: './app.component.html',
  providers:[MessageService]
})
export class AppComponent {
  title = 'catalogo-sk';

  constructor(private primeng: PrimeNG){

  }
  ngOnInit(): void {
    this.setTheme();
    this.setApplicationLanguage();
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
