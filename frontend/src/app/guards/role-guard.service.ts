import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleGuardService implements CanActivate {

  constructor(
    public router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | Observable<boolean> | Promise<boolean> {
    const expectedRoles: string = route.data['expectedRoles'];
    const token = localStorage.getItem('authToken');
    const jwt: JwtHelperService = new JwtHelperService();

    if (!token) {
      console.error('Access forbidden. Invalid token or missing user type.');
      this.router.navigate(['']);
      return false;
    }

     // Check if token is expired
     if (jwt.isTokenExpired(token)) {
      console.error('Access forbidden. Token is expired.');
      this.router.navigate(['']);
      return false;
    }

    const tokenData = jwt.decodeToken(token);

    if (tokenData.role[0].authority) {
      
      const roles: string[] = expectedRoles.split('|', 3);

      if (roles.indexOf(tokenData.role[0].authority) === -1) {
        console.error('Access forbidden. User does not have the required role.');
        this.router.navigate(['/Main-Page', 'home']);
        return of(false);
      }
    } else {
      console.error('Access forbidden. Invalid token or missing user type.');
      this.router.navigate(['']);
      return of(false);
    }

    return of(true);
  }
}
