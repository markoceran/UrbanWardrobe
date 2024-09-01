import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/models/jsonResponse';
import { Product } from 'src/app/models/product';
import { ImageService } from 'src/app/services/image.service';
import { ProductService } from 'src/app/services/product.service';
import { WishlistService } from 'src/app/services/wishlist.service';


@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css'],
})
export class MainPageComponent implements OnInit, OnDestroy  {
  
  products: Product[] = [];
  pageNumber = 0;
  currentPage = 0;
  totalElements = 0;
  totalPages = 0;
  category: string | null = null;
  private routeSub: Subscription = new Subscription();  

  constructor(private route: ActivatedRoute, private _snackBar: MatSnackBar,private wishlistService: WishlistService, private productService: ProductService, private imageService: ImageService, private sanitizer: DomSanitizer, private router: Router) { }

  ngOnInit(): void {
    // Subscribe to route parameter changes
    this.routeSub = this.route.paramMap.subscribe(params => {
      this.category = params.get('category');
      this.pageNumber = 0;  // Reset page number when category changes
      this.loadProducts();
    });
  }

  ngOnDestroy(): void {
    // Clean up the subscription to avoid memory leaks
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
  }

  loadProducts(): void {
    if (this.category === 'home') {
      this.productService.getProducts(this.pageNumber).subscribe(response => {
      this.products = [];
      this.products = response.data.map((product: Product) => ({
        ...product,
        images: [] // Ensure images is initialized
      }));
      this.currentPage = response.currentPage;
      this.totalElements = response.totalElements;
      this.totalPages = response.totalPages;

      this.products.forEach(product => {
        this.imageService.getImage(product.code, product.imagesName[0]).subscribe(blob => {
          const image = URL.createObjectURL(blob);
          product.images.push(image);
        });
      });
      });
    }else if(this.category){
      this.productService.getProductsByCategory(this.pageNumber, this.category).subscribe(response => {
        this.products = [];
        this.products = response.data.map((product: Product) => ({
          ...product,
          images: [] // Ensure images is initialized
        }));
        this.currentPage = response.currentPage;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
  
        this.products.forEach(product => {
          this.imageService.getImage(product.code, product.imagesName[0]).subscribe(blob => {
            const image = URL.createObjectURL(blob);
            product.images.push(image);
          });
        });
      });
    }
  }

  goToPage(page: number): void {
    this.pageNumber = page;
    this.loadProducts();
  }

  sanitizeUrl(url: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  openProductDetails(productCode: string){
    this.router.navigate(['/product/' + productCode]);
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3500
    });
  }

  addToWishlist(productId: number, event: Event){
    event.stopPropagation(); 
    this.wishlistService.addToWishlist(productId).subscribe(
      (response:JsonResponse) => {
        this.openSnackBar(response.message, "");
        const product = this.products.find(p => p.id === productId);
        if (product) {
          product.inWishlist = true;
        }
      },
      (error) => {
        this.openSnackBar(error.error?.message, "");
      }
    );
  }

  removeFromWishlist(productId: number, event: Event){
    event.stopPropagation(); 
    this.wishlistService.removeFromWishlist(productId).subscribe(
      (response:JsonResponse) => {
        this.openSnackBar(response.message, "");
        const product = this.products.find(p => p.id === productId);
        if (product) {
          product.inWishlist = false;
        }
      },
      (error) => {
        this.openSnackBar(error.error?.message, "");
      }
    );
  }

}
