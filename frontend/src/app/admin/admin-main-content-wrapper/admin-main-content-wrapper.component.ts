import { Component, Input } from '@angular/core';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";

@Component({
  selector: 'app-admin-main-content-wrapper',
  imports: [AdminPageTitleComponent],
  templateUrl: './admin-main-content-wrapper.component.html'
})
export class AdminMainContentWrapperComponent {

  @Input()
  styleClassObject:any;
}
