import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { BasketItem } from 'src/app/models/basketItem';
import { JsonResponse } from 'src/app/models/jsonResponse';
import { Order } from 'src/app/models/order';
import { AuthService } from 'src/app/services/auth.service';
import { ImageService } from 'src/app/services/image.service';
import { OrderService } from 'src/app/services/order.service';
import { YesNoSnackBarService } from 'src/app/services/yesNoSnackBar.service';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit {
  
  order: Order | undefined;
  role: any;

  constructor(
    private orderService: OrderService,
    private route: ActivatedRoute,
    private _snackBar: MatSnackBar,
    private sanitizer: DomSanitizer,
    private imageService: ImageService,
    private yesNoSnackBarService: YesNoSnackBarService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.role = this.authService.extractUserType();
    const orderCode = this.route.snapshot.paramMap.get('code');
    if(orderCode !== null){
      this.orderService.getByCode(orderCode).subscribe(
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

  cancelOrder(orderId:number | undefined){
    this.yesNoSnackBarService.open('Are you sure you want to cancel order?').then((result) => {
      if (result) {
        console.log('User clicked Yes');
        if(orderId != undefined){
          this.orderService.cancel(orderId).subscribe(
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
          });
        }
      } else {
        // User clicked 'No' or the SnackBar dismissed automatically
        console.log('User clicked No or SnackBar dismissed');
      }
    });
  }

  sentOrder(orderId:number | undefined){
    if(orderId != undefined){
      this.orderService.sent(orderId).subscribe(
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
      });
    }
  }

  deliverOrder(orderId:number | undefined){
    if(orderId != undefined){
      this.orderService.deliver(orderId).subscribe(
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
      });
    }
  }
}
