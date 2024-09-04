import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule} from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './components/header/header.component';
import { LoginComponent } from './components/login/login.component';
import { AuthInterceptor } from './services/auth.interceptor';
import { NgxCaptchaModule } from 'ngx-captcha';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { CreateProductComponent } from './components/create-product/create-product.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { RegisterComponent } from './components/register/register.component';
import { WishlistComponent } from './components/wishlist/wishlist.component';
import { BasketComponent } from './components/basket/basket.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ChangeShippingAddressComponent } from './components/change-shipping-address/change-shipping-address.component';
import { MatDialogModule } from '@angular/material/dialog';
import { RefillQuantityComponent } from './components/refill-quantity/refill-quantity.component';
import { CreateUserComponent } from './components/create-user/create-user.component';
import { PendingOrdersComponent } from './components/pending-orders/pending-orders.component';
import { SentOrdersComponent } from './components/sent-orders/sent-orders.component';
import { DeliveredOrdersComponent } from './components/delivered-orders/delivered-orders.component';

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    HeaderComponent,
    LoginComponent,
    CreateProductComponent,
    ProductDetailsComponent,
    RegisterComponent,
    WishlistComponent,
    BasketComponent,
    OrderDetailsComponent,
    ProfileComponent,
    ChangeShippingAddressComponent,
    RefillQuantityComponent,
    CreateUserComponent,
    PendingOrdersComponent,
    SentOrdersComponent,
    DeliveredOrdersComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatMenuModule,
    MatToolbarModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDividerModule,
    MatSnackBarModule,
    ReactiveFormsModule,
    NgxCaptchaModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule, 
    MatDialogModule
  ],
  providers:
  [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
