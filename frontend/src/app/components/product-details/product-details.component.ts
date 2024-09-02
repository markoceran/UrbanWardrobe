import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { JsonResponse } from 'src/app/models/jsonResponse';
import { Product } from 'src/app/models/product';
import { Size } from 'src/app/models/size';
import { AuthService } from 'src/app/services/auth.service';
import { BasketService } from 'src/app/services/basket.service';
import { ImageService } from 'src/app/services/image.service';
import { ProductService } from 'src/app/services/product.service';
import { RefillQuantityComponent } from '../refill-quantity/refill-quantity.component';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  product!: Product;
  selectedSize: string | null = null;
  currentSlideIndex = 0;
  role: any;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private imageService: ImageService,
    private sanitizer: DomSanitizer,
    private basketService: BasketService,
    private _snackBar: MatSnackBar,
    private authService: AuthService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.role = this.authService.extractUserType();
   
    const productCode = this.route.snapshot.paramMap.get('productCode');
    if (productCode) {
      this.productService.getProductByCode(productCode).subscribe(product => {
        this.product = product;
        this.product.images = [] 
        this.product.imagesName.forEach(imageName => {
          this.imageService.getImage(product.code, imageName).subscribe(blob => {
            const image = URL.createObjectURL(blob);
            product.images.push(image);
          });
        });
      });
    }
  }

  selectSize(size: string): void {
    this.selectedSize = size;
  }

  addToBasket(): void {
    if (this.selectedSize) {
      const size = Size[this.selectedSize as keyof typeof Size];
      this.basketService.addBasketItem(this.product.id, size).subscribe(
        (response:JsonResponse) => {
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
      alert('Please select a size.');
    }
  }

  prevSlide() {
    if (this.currentSlideIndex !== 0) {
      this.currentSlideIndex = this.currentSlideIndex - 1;
    }
  }
  
  nextSlide() {
    if (this.currentSlideIndex < this.product.images.length - 1) {
      this.currentSlideIndex = this.currentSlideIndex + 1;
    }
  }

  sanitizeUrl(url: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  get sortedSizeQuantities() {
    const sizeOrder = ['XS', 'S', 'M', 'L', 'XL', 'XXL', 'UNI'];
    return Array.from(this.product.sizeQuantities)
      .sort((a, b) => sizeOrder.indexOf(a.size) - sizeOrder.indexOf(b.size));
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action,  {
      duration: 2000
    });
  }

  refillQuantity(productId: number){
    const dialogRef = this.dialog.open(RefillQuantityComponent, {
      width: '400px',
      data: productId 
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('The dialog was closed with result:', result);
        // Optionally handle the result here
      }
    });
  }


}
