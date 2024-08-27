import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Wishlist } from 'src/app/models/wishlist';
import { AuthService } from 'src/app/services/auth.service';
import { ImageService } from 'src/app/services/image.service';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {

  wishlist: Wishlist = {
    products: [],
    id: 0
  }; // Initialize wishlist

  constructor(
    private authService: AuthService, 
    private _snackBar: MatSnackBar, 
    private sanitizer: DomSanitizer, 
    private imageService: ImageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.getUserWishlist().subscribe(
      (response: Wishlist) => {
        this.wishlist = response;
        // Ensure images are initialized as an empty array
        this.wishlist.products.forEach(product => {
          product.images = [];
          // Fetch images
          if (product.imagesName.length > 0) {
            this.imageService.getImage(product.code, product.imagesName[0]).subscribe(blob => {
              const image = URL.createObjectURL(blob);
              product.images.push(image);
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

}
