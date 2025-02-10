import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { User } from '../../../services/user.service';
import { Loja, LojaService } from '../../../services/loja.service';

@Component({
  selector: 'app-edit-loja',
  imports: [SharedModule],
  templateUrl: './edit-loja.component.html'
})
export class EditLojaComponent implements OnInit,OnDestroy {
  open = false;
  @Input({required:true})
  loja!:Loja.Loja;
  cloneLoja!:Loja.Loja;
  @Output()
  onSave = new EventEmitter<Loja.Loja>();
  loading = false;

  constructor(private lojaService:LojaService){}
  ngOnInit(): void {
    this.cloneLoja = {...this.loja};
  }

  save(){

    const payload:Loja.LojaChangebleFieldsPayload = {
      cep:this.cloneLoja.cep as string,
      endereco:this.cloneLoja.endereco as string,
      systemId:this.cloneLoja.systemId as string,
      telefone:this.cloneLoja.telefone as string
    }
    this.loading = true;
    this.lojaService.mudarLoja(payload).subscribe({
      next:()=>{
        this.loading = false;
        this.loja = {...this.cloneLoja};
        this.open = false;
        this.onSave.emit(this.loja);
      },
      error:()=>{
        alert("Erro ao atualizar loja")
        this.loading = false;
      }
    })
  }
  ngOnDestroy(): void {
    console.log("Componente destruido")
  }
}
