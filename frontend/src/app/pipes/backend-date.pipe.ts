import { Injectable, Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateToBackend'
})
@Injectable({
  providedIn: 'root'
})
export class BackendDatePipe implements PipeTransform {

  transform(value:Date): string {
    return value.toISOString().split('T')[0];
  }

}
