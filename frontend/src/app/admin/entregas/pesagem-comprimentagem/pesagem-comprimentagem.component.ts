import { Component, OnInit } from '@angular/core';
import { ProdutosService } from '../../../services/produtos.service';
import { SharedModule } from '../../../shared/shared.module';
import { UserService } from '../../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-pesagem-comprimentagem',
  imports: [SharedModule],
  templateUrl: './pesagem-comprimentagem.component.html'
})
export class PesagemComprimentagemComponent implements OnInit {

  categorias!:string[]

  constructor(
    private produtoService:ProdutosService,
    private auth:UserService
  ){}
  ngOnInit(): void {
    this.produtoService.getTermos(this.auth.getContext()?.franquia.systemId as string).subscribe({
      next:(data)=>{
        this.categorias = data.categorias.sort((a,b)=>a.localeCompare(b));
      },
      error:(e:HttpErrorResponse)=>{
        alert(e.error);
      }
    })
  }

}
