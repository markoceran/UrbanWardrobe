import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Size } from 'src/app/models/size';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-refill-quantity',
  templateUrl: './refill-quantity.component.html',
  styleUrls: ['./refill-quantity.component.css']
})
export class RefillQuantityComponent implements OnInit {

  refillForm!: FormGroup;
  sizes = Object.values(Size);

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<RefillQuantityComponent>,
    private productService: ProductService,
    private _snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) private data: number
  ){}

  ngOnInit(): void {
    this.refillForm = this.fb.group({
      size: [Size.XS, Validators.required],
      quantity: [0, Validators.required]
    });
  }

  onSubmit(): void {
    console.log('Form submitted');
    if (this.refillForm.valid) {
      const { size, quantity } = this.refillForm.value;
      this.productService.refillQuantity(this.data, size, quantity).subscribe(
        (message) => {
          this.dialogRef.close(this.refillForm.value);
          this.openSnackBar(message.message, "");
          console.log(message.message);
          setTimeout(() => {
            window.location.reload();
          }, 700);
        },
        (error) => {
          this.dialogRef.close(this.refillForm.value);
          this.openSnackBar(error.message, "");
          console.error(error.message);
          setTimeout(() => {
            window.location.reload();
          }, 2000);
        }
      );
    }
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action,  {
      duration: 3500
    });
  }


}
