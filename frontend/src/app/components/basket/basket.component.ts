import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Basket } from 'src/app/models/basket';
import { AuthService } from 'src/app/services/auth.service';
import { ImageService } from 'src/app/services/image.service';

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

  selectedBasketItemId: number | null = null;

  constructor(
    private authService: AuthService, 
    private _snackBar: MatSnackBar, 
    private sanitizer: DomSanitizer, 
    private imageService: ImageService,
    private router: Router
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

          // Fetch images
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

  openFormForEdit(basketItemId: number): void {
    this.selectedBasketItemId = this.selectedBasketItemId === basketItemId ? null : basketItemId;
  }

}
