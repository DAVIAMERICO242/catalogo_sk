import { Component } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-entregas',
  imports: [SharedModule, AdminPageTitleComponent,RouterModule],
  templateUrl: './entregas.component.html'
})
export class EntregasComponent {

}
