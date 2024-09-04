import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { JsonResponse } from 'src/app/models/jsonResponse';
import { NewUser } from 'src/app/models/newUser';
import { Role } from 'src/app/models/role';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

  roles = Object.values(Role);

  formGroup: FormGroup = new FormGroup({
    email: new FormControl(''),
    password: new FormControl(''),
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    phoneNumber: new FormControl(''),
    role: new FormControl('')
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
      role: ['WORKER', [Validators.required]],
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

    const role = this.formGroup.get('role')?.value;

    if(role === 'WORKER'){
      this.authService.createWorker(user).subscribe(
      (response:JsonResponse) => {
        this.openSnackBar(response.message, "");
        setTimeout(() => {
          window.location.reload();
        }, 1500);
      },
      (error) => {
        this.formGroup.setErrors({ registrationFailed: true });
        this.openSnackBar(error.error?.message, "");
      }
      );
    }else if(role === 'ADMIN'){
      this.authService.createAdmin(user).subscribe(
        (response:JsonResponse) => {
          this.openSnackBar(response.message, "");
          setTimeout(() => {
            window.location.reload();
          }, 1500);
        },
        (error) => {
          this.formGroup.setErrors({ registrationFailed: true });
          this.openSnackBar(error.error?.message, "");
        }
      );
    }else {
      this.authService.createCourier(user).subscribe(
        (response:JsonResponse) => {
          this.openSnackBar(response.message, "");
          setTimeout(() => {
            window.location.reload();
          }, 1500);
        },
        (error) => {
          this.formGroup.setErrors({ registrationFailed: true });
          this.openSnackBar(error.error?.message, "");
        }
      );
    }
  
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3500
    });
  }
}
