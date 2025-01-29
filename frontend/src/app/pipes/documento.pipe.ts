import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'documentoPipe'
})
export class DocumentoPipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    if (!value) return '';

    const strValue = value.toString().replace(/\D/g, ''); // Remove não numéricos

    if (strValue.length === 11) {
      // Formatar como CPF: 000.000.000-00
      return strValue.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    } else if (strValue.length === 14) {
      // Formatar como CNPJ: 00.000.000/0000-00
      return strValue.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
    }

    return value.toString(); // Retorna original se não for CPF nem CNPJ;
  }

}
