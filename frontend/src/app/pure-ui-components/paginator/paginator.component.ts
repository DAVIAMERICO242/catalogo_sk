import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PaginatorModule } from 'primeng/paginator';

@Component({
  selector: 'app-paginator',
  imports: [PaginatorModule],
  templateUrl: './paginator.component.html'
})
export class PaginatorComponent{
  
  @Input({required:true})
  rowsPerPage!:number;
  @Input({required:true})
  totalRecordsUnpaged!:number;
  @Input({required:true})
  currentPage!:number;
  @Output()
  pageChange = new EventEmitter<number>();

  emitChange(e:any){
    this.pageChange.emit(e.page);
  }

}
