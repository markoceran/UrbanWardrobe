import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Basket } from 'src/app/models/basket';
import { JsonResponse } from 'src/app/models/jsonResponse';
import { AuthService } from 'src/app/services/auth.service';
import { BasketService } from 'src/app/services/basket.service';
import { ImageService } from 'src/app/services/image.service';
import { OrderService } from 'src/app/services/order.service';
import { YesNoSnackBarService } from 'src/app/services/yesNoSnackBar.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css']
})
export class BasketComponent implements OnInit {

  basket: Basket = {
    basketItems: [],
    id: 0
  }; // Initialize basket

  productsAmount:number = 0;
  shippingAmount:number = 0;
  totalAmount:number = 0;

  constructor(
    private authService: AuthService, 
    private _snackBar: MatSnackBar, 
    private sanitizer: DomSanitizer, 
    private imageService: ImageService,
    private router: Router,
    private yesNoSnackBarService: YesNoSnackBarService,
    private basketService: BasketService,
    private orderService: OrderService
  ) { }

  ngOnInit(): void {
    this.authService.getUserBasket().subscribe(
      (response: Basket) => {
        this.basket = response;
        // Ensure images are initialized as an empty array
        this.basket.basketItems.forEach(basketItem => {

          basketItem.product.images = [];
          this.productsAmount = this.productsAmount + (basketItem.product.price * basketItem.quantity);
          if(this.productsAmount > 100){
            this.shippingAmount = 0;
          }else{
            this.shippingAmount = 3;
          }
          this.totalAmount = this.productsAmount + this.shippingAmount;

          if (basketItem.product.imagesName.length > 0) {
            this.imageService.getImage(basketItem.product.code, basketItem.product.imagesName[0]).subscribe(blob => {
              const image = URL.createObjectURL(blob);
              basketItem.product.images.push(image);
            }, () => {
              this.openSnackBar('Error loading images.', '');
            });
          }
        });
      },
      () => {
        this.openSnackBar("Can't get user details.", '');
      }
    );
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3500
    });
  }

  sanitizeUrl(url: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  openProductDetails(productCode: string){
    this.router.navigate(['/product/' + productCode]);
  }

  deleteBasketItem(basketItemId: number): void {
    this.yesNoSnackBarService.open('Are you sure you want to delete basket item?').then((result) => {
      if (result) {
        console.log('User clicked Yes');
        this.basketService.removeBasketItem(basketItemId).subscribe(
          (response: JsonResponse) => {
            this.openSnackBar(response.message, "");
            setTimeout(() => {
              window.location.reload();
            }, 1000);
          },
          (error) => {
            this.openSnackBar(error.error?.message, "");
            setTimeout(() => {
              window.location.reload();
            }, 2000);
          }
        );
      } else {
        // User clicked 'No' or the SnackBar dismissed automatically
        console.log('User clicked No or SnackBar dismissed');
      }
    });
  }

  decreaseQuantity(basketItemId: number): void {
    this.basketService.decreaseQuantityFromBasketItem(basketItemId).subscribe(
        (response: JsonResponse) => {
          this.openSnackBar(response.message, "");
          setTimeout(() => {
            window.location.reload();
          }, 1000);
        },
        (error) => {
          this.openSnackBar(error.error?.message, "");
          setTimeout(() => {
            window.location.reload();
          }, 2000);
        }
    );
  }

  createOrder(){
    if(this.basket.basketItems.length > 0){
      this.orderService.create().subscribe(
      (response: JsonResponse) => {
        this.openSnackBar(response.message, "");
        setTimeout(() => {
          window.location.reload();
        }, 1300);
      },
      (error) => {
        this.openSnackBar(error.error?.message, "");
        setTimeout(() => {
          window.location.reload();
        }, 4000);
      });
    }
  }

}
