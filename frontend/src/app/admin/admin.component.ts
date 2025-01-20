import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AdminNavComponent } from "./admin-nav/admin-nav.component";

@Component({
  selector: 'app-admin',
  imports: [RouterModule, AdminNavComponent],
  templateUrl: './admin.component.html'
})
export class AdminComponent {

}
