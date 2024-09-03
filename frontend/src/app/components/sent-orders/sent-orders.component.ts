import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Order } from 'src/app/models/order';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-sent-orders',
  templateUrl: './sent-orders.component.html',
  styleUrls: ['./sent-orders.component.css']
})
export class SentOrdersComponent implements OnInit {

  orders: Order[] = [];
  pageNumber = 0;
  currentPage = 0;
  totalElements = 0;
  totalPages = 0;

  constructor(private orderService: OrderService, private _snackBar: MatSnackBar, private router: Router) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(){
    this.orderService.getSentOrders(this.pageNumber).subscribe(
      (response: any) => {
        this.orders = response.data;
        this.orders.forEach(order => {
          order.creationTime = this.convertToDate(order.creationTime.toString());
        })
        this.currentPage = response.currentPage;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
      },
      (error) => {
        this.openSnackBar(error.error?.message, "");
    });
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3500
    });
  }

  goToPage(page: number): void {
    this.pageNumber = page;
    this.loadOrders();
  }

  convertToDate(dateString: string): Date {
    const dateParts = dateString.split(',').map(part => parseInt(part, 10));
    return new Date(
      dateParts[0], // Year
      dateParts[1] - 1, // Month (0-indexed)
      dateParts[2], // Day
      dateParts[3], // Hour
      dateParts[4], // Minute
      dateParts[5], // Second
      dateParts[6] / 1000000 // Convert nanoseconds to milliseconds (since JavaScript Date uses milliseconds)
    );
  }

  openOrderDetails(code:string){
    this.router.navigate(['/order/' + code]);
  }

}
