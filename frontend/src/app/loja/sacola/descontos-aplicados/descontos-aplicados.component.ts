import { Component, Input, OnInit } from '@angular/core';
import { Sacola, SacolaService } from '../../../services/sacola.service';
import { Desconto } from '../../../services/descontos.service';
import { SharedModule } from '../../../shared/shared.module';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-descontos-aplicados',
  imports: [SharedModule],
  templateUrl: './descontos-aplicados.component.html'
})
export class DescontosAplicadosComponent implements OnInit{
  @Input({required:true})
  rawSacola!:Sacola.RawSacola;
  totalDescontos = 0;
  open = false;
  descontosResumidos!:Desconto.DescontoSummary[];
  descontosAplicados!:Desconto.DescontoAplicado[];
  subscriptions = new Subscription();
  constructor(private sacolaService:SacolaService){}
  ngOnInit(): void {
    this.subscriptions.add(this.sacolaService.descontosAplicados$.subscribe((data)=>{
        this.descontosAplicados = data;
        this.totalDescontos = this.descontosAplicados.reduce((a,b)=>a+b.valorAplicado,0);
        this.setResumo();
    }));
  }

  setResumo(){
    this.descontosResumidos = Object.values(
      this.descontosAplicados.reduce((acc, desconto) => {
        if (!acc[desconto.nome]) {
          acc[desconto.nome] = { nome: desconto.nome, quantidadeAplicacoes: 0, valor: 0 };
        }
        acc[desconto.nome].quantidadeAplicacoes++;
        acc[desconto.nome].valor += desconto.valorAplicado;
        return acc;
      }, {} as Record<string, Desconto.DescontoSummary>)
    );
  }

  forceType(row:any){
    return row as Desconto.DescontoSummary;
  }
}
