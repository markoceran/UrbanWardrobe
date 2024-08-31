import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UserDTO } from 'src/app/models/userDTO';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  
  user: UserDTO | undefined; 
  selectedTab = 'personalInformation';

  constructor(private authService: AuthService, private _snackBar: MatSnackBar, private router: Router) { }

  ngOnInit(): void {
    this.authService.getUserProfile().subscribe(
      (response: UserDTO) => {
        this.user = response;
        this.user.orders.forEach(order => {
          order.creationTime = this.convertToDate(order.creationTime.toString());
        })
      },
      (error) => {
        this.openSnackBar(error.error?.message, "");
    });
  }


  selectTab(tab: string) {
    this.selectedTab = tab;
  }

  editProfile() {
    
  }

  logout() {
    this.authService.logout();
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3500
    });
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

  openOrderDetails(id:number){
    this.router.navigate(['/order/' + id]);
  }
}
