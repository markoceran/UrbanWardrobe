import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuardService implements CanActivate{

  constructor(
		public authService: AuthService,
		public router: Router
	) { }

	canActivate(): boolean {
		if (this.authService.isLoggedIn()) {
			return true;
		}
		return false;
	}

	
}
