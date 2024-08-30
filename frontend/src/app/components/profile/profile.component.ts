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
      },
      (error) => {
        this.openSnackBar(error.error?.message, "");
    });
  }


  selectTab(tab: string) {
    this.selectedTab = tab;
  }

  editProfile() {
    // Logic to edit profile
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['']);
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 3500
    });
  }
}
