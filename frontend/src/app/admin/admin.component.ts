import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AdminNavComponent } from "./admin-nav/admin-nav.component";
import { SharedModule } from '../shared/shared.module';
import { AdminMainContentWrapperComponent } from "./admin-main-content-wrapper/admin-main-content-wrapper.component";

@Component({
  selector: 'app-admin',
  imports: [RouterModule, AdminNavComponent, SharedModule, AdminMainContentWrapperComponent],
  templateUrl: './admin.component.html'
})
export class AdminComponent {
  expanded = false;
}
