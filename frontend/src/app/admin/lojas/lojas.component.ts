import { Component, OnInit } from '@angular/core';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { User, UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';
import { EditLojaComponent } from "./edit-loja/edit-loja.component";
import { Loja, LojaService } from '../../services/loja.service';

@Component({
  selector: 'app-lojas',
  imports: [AdminPageTitleComponent, SharedModule, EditLojaComponent],
  templateUrl: './lojas.component.html'
})
export class LojasComponent implements OnInit{
  lojas!:Loja.Loja[];//se eu ainda estiver viver e deus nao tiver me matado aqui será Loja.loja[]
  constructor(private lojaService:LojaService,private userService:UserService){}
  ngOnInit(): void {//o localstorage tem tipagem pessimista por isso o 'as'
    const role = this.userService.getContext()?.role;
    const loja = this.userService.getContext()?.loja;
    if(role===User.Role.OPERACIONAL){
      this.lojaService.getById(loja?.systemId as string).subscribe({
        next:(data)=>{
          this.lojas = [data];
        }
      })
    }else{
      const franquiaId = this.userService.getContext()?.franquia.systemId as string;
      this.lojaService.getLojasByFranquiaId(franquiaId).subscribe({
        next:(data)=>{
          this.lojas = data;
        }
      })
    }
  }
  forceType(row:any){
    return row as Loja.Loja;
  }

  onEdit(loja:Loja.Loja){
    const index = this.lojas.findIndex(l => l.systemId === loja.systemId);
    if (index !== -1) {
      this.lojas[index] = { ...this.lojas[index], ...loja };
    }
  }

}
