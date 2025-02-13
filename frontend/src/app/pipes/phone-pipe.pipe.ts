import { Injectable, Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'phonePipe'
})
@Injectable({providedIn:"root"})
export class PhonePipePipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    if (!value) return '';

    let phone = value.toString().replace(/\D/g, ''); // Remove caracteres não numéricos

    if (phone.length === 10) {
      // Número sem o 9 (fixo ou celular antigo) -> (XX) XXXX-XXXX
      return phone.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
    } else if (phone.length === 11) {
      // Número com o 9 na frente (celular) -> (XX) 9XXXX-XXXX
      return phone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    }

    return value.toString(); // Retorna o original se não for um número válido
  }

}
