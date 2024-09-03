import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginDTO } from 'src/app/dto/loginDTO';
import { AuthService } from 'src/app/services/auth.service';
import {MatSnackBar} from "@angular/material/snack-bar";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  
  formGroup: FormGroup = new FormGroup({
    email: new FormControl(''),
    password: new FormControl('')
  });

  constructor(
    private authService: AuthService,
    private router: Router,
    private formBuilder: FormBuilder,
    private _snackBar: MatSnackBar,
  ) { }

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  get loginGroup(): { [key: string]: AbstractControl } {
    return this.formGroup.controls;
  }

  onSubmit() {
    if (this.formGroup.invalid) {
      // Mark all controls as touched to trigger validation messages
      this.formGroup.markAllAsTouched();
      return;
    }

    let login: LoginDTO = new LoginDTO();
    login.email = this.formGroup.get('email')?.value;
    login.password = this.formGroup.get('password')?.value;
    
    this.authService.Login(login).subscribe({
      next: (token: string) => {
        localStorage.setItem('authToken', token);
        const role = this.authService.extractUserType();
        if(role !== null && role === "WORKER"){
          this.router.navigate(['/pendingOrders']);
        }else{
          this.router.navigate(['/Main-Page', 'home']);
        }
        
      },
      error: (error) => {
        this.formGroup.setErrors({ unauthenticated: true });
        this.openSnackBar("The username or password is incorrect!", "");
      }
    });
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3500
    });
  }

}
