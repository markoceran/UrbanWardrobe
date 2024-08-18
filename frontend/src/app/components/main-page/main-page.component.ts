import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Product } from 'src/app/models/product';
import { ProductWithImages } from 'src/app/models/productWithImages';
import { ImageService } from 'src/app/services/image.service';
import { ProductService } from 'src/app/services/product.service';


@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css'],
})
export class MainPageComponent implements OnInit  {
  
  products: ProductWithImages[] = [];
  pageNumber = 0;
  currentPage = 0;
  totalElements = 0;
  totalPages = 0;

  constructor(private productService: ProductService, private imageService: ImageService, private sanitizer: DomSanitizer, private router: Router) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts(this.pageNumber).subscribe(response => {
      this.products = response.data.map((product: ProductWithImages) => ({
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

}
