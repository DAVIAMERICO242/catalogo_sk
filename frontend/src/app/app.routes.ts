import { Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';

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
            },{
                path:"lojas",
                loadComponent:()=>import("./admin/lojas/lojas.component").then(m=>m.LojasComponent)
            }
            ,{
                path:"entregas",
                loadComponent:()=>import("./admin/entregas/entregas.component").then(m=>m.EntregasComponent),
                children:[
                    {
                        path:"",
                        redirectTo:"integracao-correios",
                        pathMatch:"full"
                    },{
                        path:"integracao-correios",
                        loadComponent:()=>import("./admin/entregas/configuracao-franquia/configuracao-franquia.component").then(m=>m.ConfiguracaoFranquiaComponent),
                    },{
                        path:"pesagem",
                        loadComponent:()=>import("./admin/entregas/pesagem/pesagem.component").then(m=>m.PesagemComponent),
                    }
                    ,{
                        path:"dimensoes",
                        loadComponent:()=>import("./admin/entregas/dimensoes/dimensoes.component").then(m=>m.DimensoesComponent),
                    },
                    {
                        path:"faixas-cep",
                        loadComponent:()=>import("./admin/entregas/faixas-cep/faixas-cep.component").then(m=>m.FaixasCepComponent),
                    }
                ]
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
