import { Component, Input } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-bulk-banner-replication',
  imports: [SharedModule],
  templateUrl: './bulk-banner-replication.component.html'
})
export class BulkBannerReplicationComponent {

  @Input({required:true})
  stringOrBase64Desktop:string|undefined = "";
  @Input({required:true})
  stringOrBase64Mobile:string|undefined  = "";


  

}
