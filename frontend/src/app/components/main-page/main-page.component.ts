import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {MatSnackBar} from "@angular/material/snack-bar";
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { Product } from 'src/app/models/product';
import { ProductService } from 'src/app/services/product.service';


@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css'],
})
export class MainPageComponent implements OnInit  {
  
  products: Product[] = [];
  pageNumber = 0;
  currentPage = 0;
  totalElements = 0;
  totalPages = 0;

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts(this.pageNumber).subscribe(response => {
      this.products = response.data;
      this.currentPage = response.currentPage;
      this.totalElements = response.totalElements;
      this.totalPages = response.totalPages;
    });
  }

  goToPage(page: number): void {
    this.pageNumber = page;
    this.loadProducts();
  }

}
