import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JsonResponse } from 'src/app/models/jsonResponse';
import { Product } from 'src/app/models/product';
import { AuthService } from 'src/app/services/auth.service';
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
  role: any;

  constructor(private authService: AuthService, private route: ActivatedRoute, private _snackBar: MatSnackBar,private wishlistService: WishlistService, private productService: ProductService, private imageService: ImageService, private sanitizer: DomSanitizer, private router: Router) { }

  ngOnInit(): void {
    this.role = this.authService.extractUserType();
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

      if(this.role === 'ADMIN'){
        this.productService.getAllProducts(this.pageNumber).subscribe(response => {
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
      }else{
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
      }
      
    }else if(this.category === 'search'){

      this.route.paramMap.subscribe(params => {
        const code = params.get('code');
        if(code !== null){
          if(this.role === "ADMIN"){
            this.productService.searchAllByCode(code).subscribe(
              (response: any) => {
                  this.products = [];
                  this.products = response.map((product: Product) => ({
                    ...product,
                    images: [] // Ensure images is initialized
                  }));
                  this.products.forEach(product => {
                    this.imageService.getImage(product.code, product.imagesName[0]).subscribe(blob => {
                      const image = URL.createObjectURL(blob);
                      product.images.push(image);
                  });
              });
              },
              (error) => {
                this.openSnackBar("Error during search", "");
            });
          }else {
            this.productService.searchByCode(code).subscribe(
              (response: any) => {
                  this.products = [];
                  this.products = response.map((product: Product) => ({
                    ...product,
                    images: [] // Ensure images is initialized
                  }));
                  this.products.forEach(product => {
                    this.imageService.getImage(product.code, product.imagesName[0]).subscribe(blob => {
                      const image = URL.createObjectURL(blob);
                      product.images.push(image);
                  });
              });
              },
              (error) => {
                this.openSnackBar("Error during search", "");
            });
          }
          
        }
      });

    }else if(this.category){

      if(this.role === 'ADMIN'){
        this.productService.getAllProductsByCategory(this.pageNumber, this.category).subscribe(response => {
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
      }else{
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
