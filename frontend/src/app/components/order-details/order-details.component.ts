import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { BasketItem } from 'src/app/models/basketItem';
import { Order } from 'src/app/models/order';
import { ImageService } from 'src/app/services/image.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit {
  
  order: Order | undefined;

  constructor(
    private orderService: OrderService,
    private route: ActivatedRoute,
    private _snackBar: MatSnackBar,
    private sanitizer: DomSanitizer,
    private imageService: ImageService
  ) { }

  ngOnInit(): void {
    const orderId = Number(this.route.snapshot.paramMap.get('id'));
    this.orderService.getById(orderId).subscribe(
      (response: Order) => {
        this.order = response;
        this.order.creationTime = this.convertToDate(this.order.creationTime.toString());
        this.order?.basketItems.forEach(item => {
          item.product.images = [];
          if (item.product.imagesName && item.product.imagesName.length > 0) {
            this.imageService.getImage(item.product.code, item.product.imagesName[0]).subscribe(blob => {
              const image = URL.createObjectURL(blob);
              item.product.images.push(image);
            });
          } else {
            console.warn('imagesName is empty or undefined for product:', item.product.code);
          }
        });    
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

  sanitizeUrl(url: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(url);
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
}
