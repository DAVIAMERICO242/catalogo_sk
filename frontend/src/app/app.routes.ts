import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ProdutosComponent } from './admin/produtos/produtos.component';
import { AuthGuard } from './auth.guard';
import { PedidosComponent } from './admin/pedidos/pedidos.component';
import { CatalogoComponent as CatalogoAdmin} from './admin/catalogo/catalogo.component';
import { CatalogoComponent } from './catalogo-choose/catalogo.component';

export const routes: Routes = [
    {
        path:"",
        redirectTo:"matriz",
        pathMatch:"full",
    },
    {
        path:"franquia",
        loadComponent:()=>import("./catalogo-choose/catalogo.component").then((m)=>m.CatalogoComponent),
    },
    {
        path:"matriz",
        loadComponent:()=>import("./catalogo-choose/catalogo.component").then((m)=>m.CatalogoComponent),
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
        loadComponent: () => import("./loja/main/main.component").then(m => m.LojaComponent),
        children:[
            {
                path:"",
                loadComponent: () => import("./loja/product-grid/product-grid.component").then(m => m.ProductGridComponent),
            },{
                path:"produto-catalogo",
                children:[{
                    path:":id",
                    loadComponent:()=>import("./loja/product-page/product-page.component").then(m=>m.ProductPageComponent)
                }]
            },{
                path:"checkout",
                loadComponent:()=>import("./loja/checkout/checkout.component").then(m=>m.CheckoutComponent)
            }
        ]
    },
];
