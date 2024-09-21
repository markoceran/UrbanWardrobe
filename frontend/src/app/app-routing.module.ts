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
import { ProfileComponent } from './components/profile/profile.component';
import { CreateUserComponent } from './components/create-user/create-user.component';
import { PendingOrdersComponent } from './components/pending-orders/pending-orders.component';
import { SentOrdersComponent } from './components/sent-orders/sent-orders.component';
import { DeliveredOrdersComponent } from './components/delivered-orders/delivered-orders.component';
import { RoleGuardService } from './guards/role-guard.service';



const routes: Routes = [
  {
    path: 'Main-Page/:category',
    component: MainPageComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'ADMIN|USER' }
  },
  {
    path: 'Main-Page/:category/:code',
    component: MainPageComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'ADMIN|USER' }
  },
  {
    path: '',
    component: LoginComponent
  },
  {
    path: 'createProduct',
    component: CreateProductComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'ADMIN' }
  },
  {
    path: 'product/:productCode',
    component: ProductDetailsComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'ADMIN|USER' }
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'wishlist',
    component: WishlistComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'USER' }
  },
  {
    path: 'basket',
    component: BasketComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'USER' }
  },
  {
    path: 'order/:code',
    component: OrderDetailsComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'COURIER|USER|WORKER' }
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'USER' }
  },
  {
    path: 'createUser',
    component: CreateUserComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'ADMIN' }
  },
  {
    path: 'pendingOrders',
    component: PendingOrdersComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'WORKER' }
  },
  {
    path: 'sentOrders',
    component: SentOrdersComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'WORKER|COURIER' }
  },
  {
    path: 'deliveredOrders',
    component: DeliveredOrdersComponent,
    canActivate: [RoleGuardService], 
    data: { expectedRoles: 'COURIER' }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
