import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'datetimeBrazil'
})
export class DatetimeBrazilPipe implements PipeTransform {

  transform(value: Date | undefined | string, ...args: unknown[]): string | null {
    if (!value) return null;

    // Ensure the input is a valid Date
    const date = new Date(value);
    if (isNaN(date.getTime())) {
      return null; // Return null for invalid dates
    }

    // Format to dd/MM/yyyy HH:mm:ss
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Months are 0-based
    const year = date.getFullYear();

    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');

    return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
  }

}
