import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ShippingAddress } from 'src/app/models/shippingAddress';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-change-shipping-address',
  templateUrl: './change-shipping-address.component.html',
  styleUrls: ['./change-shipping-address.component.css']
})
export class ChangeShippingAddressComponent implements OnInit {

  editForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ChangeShippingAddressComponent>,
    private authService: AuthService,
    private _snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) private data: ShippingAddress | undefined
  ){}

  ngOnInit(): void {
    this.editForm = this.fb.group({
      country: [this.data?.country, Validators.required],
      city: [this.data?.city, Validators.required],
      street: [this.data?.street, Validators.required],
      number: [this.data?.number, Validators.required],
      postalCode: [this.data?.postalCode, Validators.required]
    });
  }

  onSubmit(): void {
    console.log('Form submitted');
    if (this.editForm.valid) {
      const formValues = this.editForm.value;
      this.authService.updateShippingAddress(formValues).subscribe(
        (message) => {
          this.dialogRef.close(this.editForm.value);
          this.openSnackBar(message.message, "");
          console.log(message.message);
          setTimeout(() => {
            window.location.reload();
          }, 700);
          
        },
        (error) => {
          this.dialogRef.close(this.editForm.value);
          this.openSnackBar(error.error?.message, "");
          console.error(error.error?.message);
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
