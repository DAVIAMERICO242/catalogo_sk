import { Routes } from '@angular/router';
import { CatalogoComponent } from './catalogo/catalogo.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ProdutosComponent } from './admin/produtos/produtos.component';
import { AuthGuard } from './auth.guard';
import { PedidosComponent } from './admin/pedidos/pedidos.component';

export const routes: Routes = [
    {
        path:"",
        component:CatalogoComponent
    },
    {
        path:"login",
        component:LoginComponent
    },
    {
        path:"admin",
        component:AdminComponent,
        canActivate:[AuthGuard],
        children:[
            {
                path:"",
                redirectTo:"produtos",
                pathMatch:"full"
            },
            {
                path:"produtos",
                component:ProdutosComponent
            },
            {
                path:"catalogo",
                component:CatalogoComponent
            },{
                path:"pedidos",
                component:PedidosComponent
            }
        ]
    }
];
