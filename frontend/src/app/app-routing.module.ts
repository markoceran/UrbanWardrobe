import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import {LoginGuardService} from "./guards/login-guard.service";
import { CreateProductComponent } from './components/create-product/create-product.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { RegisterComponent } from './components/register/register.component';
import { WishlistComponent } from './components/wishlist/wishlist.component';
import { BasketComponent } from './components/basket/basket.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';


const routes: Routes = [
  {
    path: 'Main-Page',
    component: MainPageComponent
  },
  {
    path: '',
    component: LoginComponent,
    canActivate: [LoginGuardService]
  },
  {
    path: 'createProduct',
    component: CreateProductComponent
  },
  {
    path: 'product/:productCode',
    component: ProductDetailsComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'wishlist',
    component: WishlistComponent
  },
  {
    path: 'basket',
    component: BasketComponent
  },
  {
    path: 'order/:id',
    component: OrderDetailsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
