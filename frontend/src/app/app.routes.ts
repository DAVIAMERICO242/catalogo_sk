import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ProdutosComponent } from './admin/produtos/produtos.component';
import { AuthGuard } from './auth.guard';
import { PedidosComponent } from './admin/pedidos/pedidos.component';
import { CatalogoComponent as CatalogoAdmin} from './admin/catalogo/catalogo.component';
import { CatalogoComponent } from './catalogo/catalogo.component';

export const routes: Routes = [
    {
        path:"",
        redirectTo:"matriz",
        pathMatch:"full",
    },
    {
        path:"franquia",
        loadComponent:()=>import("./catalogo/catalogo.component").then((m)=>m.CatalogoComponent),
    },
    {
        path:"matriz",
        loadComponent:()=>import("./catalogo/catalogo.component").then((m)=>m.CatalogoComponent),
    },
    {
        path:"login",
        loadComponent:()=>import("./login/login.component").then(m=>m.LoginComponent)
    },
    {
        path:"admin",
        loadComponent:()=>import("./admin/admin.component").then(m=>m.AdminComponent),
        canActivate:[AuthGuard],
        children:[
            {
                path:"",
                redirectTo:"produtos",
                pathMatch:"full",
            },
            {
                path:"produtos",
                loadComponent:()=>import("./admin/produtos/produtos.component").then(m=>m.ProdutosComponent)
            },
            {
                path:"catalogo",
                loadComponent:()=>import("./admin/catalogo/catalogo.component").then(m=>m.CatalogoComponent),
            },{
                path:"pedidos",
                loadComponent:()=>import("./admin/pedidos/pedidos.component").then(m=>m.PedidosComponent)
            }
            ,{
                path:"banners",
                loadComponent:()=>import("./admin/banners/banners.component").then(m=>m.BannersComponent)
            }
        ]
    },
    {
        path: ":slug",
        loadComponent: () => import("./catalogo/catalogo-loja/catalogo-loja.component").then(m => m.CatalogoLojaComponent)
    },
];
