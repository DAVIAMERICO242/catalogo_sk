import { Component, OnInit } from '@angular/core';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { User, UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';

@Component({
  selector: 'app-lojas',
  imports: [AdminPageTitleComponent, SharedModule],
  templateUrl: './lojas.component.html'
})
export class LojasComponent implements OnInit{
  lojas!:User.Loja[];//se eu ainda estiver viver e deus nao tiver me matado aqui será Loja.loja[]
  constructor(private userService:UserService){}
  ngOnInit(): void {//o localstorage tem tipagem pessimista por isso o 'as'
    const role = this.userService.getContext()?.role;
    if(role===User.Role.ADMIN){
      this.lojas = this.userService.getContext()?.lojasFranquia as User.Loja[];
    }else{
      this.lojas = [this.userService.getContext()?.loja] as User.Loja[];
    }
  }
  forceType(row:any){
    return row as User.Loja;
  }
}
