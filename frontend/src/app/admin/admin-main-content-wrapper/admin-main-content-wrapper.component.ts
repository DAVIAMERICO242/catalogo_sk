import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-admin-main-content-wrapper',
  imports: [],
  templateUrl: './admin-main-content-wrapper.component.html'
})
export class AdminMainContentWrapperComponent {

  @Input()
  styleClassObject:any;
}
