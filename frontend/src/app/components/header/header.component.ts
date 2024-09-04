import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Product } from 'src/app/models/product';
import { AuthService } from 'src/app/services/auth.service';
import { ProductService } from 'src/app/services/product.service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{

  role: any;
  showSearchInput: boolean = false;

  constructor(private productService: ProductService, private authService:AuthService, private _snackBar: MatSnackBar, private router: Router) { }

  
  ngOnInit(): void {
    this.role = this.authService.extractUserType();
    console.log(this.role);
  }


  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout(){
    this.authService.logout().subscribe(
      () => {
        localStorage.clear();
        this.router.navigate(['']);
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

  clickSearch(){
    this.showSearchInput = !this.showSearchInput;
  }

  onEnter(event: Event): void {
    const inputValue = (event.target as HTMLInputElement).value;
    console.log('Enter pressed, input value:', inputValue);

    this.router.navigate(['/Main-Page', 'search', inputValue]);
  }

}
