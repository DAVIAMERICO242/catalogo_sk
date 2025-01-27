import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateBrazil'
})
export class DateBrazilPipe implements PipeTransform {

  private datePipe = new DatePipe('en-US');  // Instantiate DatePipe directly


  transform(value: Date | string | number, ...args: unknown[]): unknown {
    if (!value) return null;
    return this.datePipe.transform(value, 'dd/MM/yyyy');
  }

}
