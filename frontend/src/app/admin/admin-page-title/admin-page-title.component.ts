import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-admin-page-title',
  imports: [],
  templateUrl: './admin-page-title.component.html'
})
export class AdminPageTitleComponent {
  @Input({required:true})
  title!:string;

  @Input({required:true})
  iconClass!:string;
}
