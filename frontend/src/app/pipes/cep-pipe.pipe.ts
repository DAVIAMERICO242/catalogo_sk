import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'cepPipe'
})
export class CepPipePipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    if (!value) return '';

    let cep = value.toString().replace(/\D/g, ''); // Remove caracteres não numéricos

    if (cep.length === 8) {
      return cep.replace(/(\d{5})(\d{3})/, '$1-$2'); // Formata XXXXX-XXX
    }

    return value.toString(); // Retorna o original se não for um CEP válido
  }

}
