import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { JsonResponse } from 'src/app/models/jsonResponse';
import { NewUser } from 'src/app/models/newUser';
import { UserDTO } from 'src/app/models/userDTO';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  formGroup: FormGroup = new FormGroup({
    email: new FormControl(''),
    password: new FormControl(''),
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    phoneNumber: new FormControl(''),
    street: new FormControl(''),
    city: new FormControl(''),
    number: new FormControl(0),
    postalCode: new FormControl(''),
    country: new FormControl('')
  });

  constructor(
    private authService: AuthService,
    private router: Router,
    private formBuilder: FormBuilder,
    private _snackBar: MatSnackBar,
  ) { }

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      phoneNumber: ['', [Validators.required]],
      street: ['', [Validators.required]],
      city: ['', [Validators.required]],
      number: [0, [Validators.required, Validators.min(1)]],
      postalCode: ['', [Validators.required]],
      country: ['', [Validators.required]]
    });
  }

  get registerGroup(): { [key: string]: AbstractControl } {
    return this.formGroup.controls;
  }

  onSubmit() {
    if (this.formGroup.invalid) {
      this.formGroup.markAllAsTouched();
      return;
    }


    let user: NewUser = new NewUser();
    user.email = this.formGroup.get('email')?.value;
    user.password = this.formGroup.get('password')?.value;
    user.firstName = this.formGroup.get('firstName')?.value;
    user.lastName = this.formGroup.get('lastName')?.value;
    user.phoneNumber = this.formGroup.get('phoneNumber')?.value;
    user.shippingAddress = {
      street: this.formGroup.get('street')?.value,
      city: this.formGroup.get('city')?.value,
      number: this.formGroup.get('number')?.value,
      postalCode: this.formGroup.get('postalCode')?.value,
      country: this.formGroup.get('country')?.value
    };

    this.authService.register(user).subscribe(
      (response:JsonResponse) => {
        this.openSnackBar(response.message, "");
        this.router.navigate(['']);
      },
      (error) => {
        this.formGroup.setErrors({ registrationFailed: true });
        this.openSnackBar(error.error?.message, "");
      }
    );
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3500
    });
  }
}
