import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { LoginDTO } from "../dto/loginDTO";
import { jwtDecode } from 'jwt-decode';
import { UserDTO } from "../models/userDTO";
import { JsonResponse } from "../models/jsonResponse";
import { Wishlist } from "../models/wishlist";
import { Basket } from "../models/basket";
import { Router } from "@angular/router";
import { ShippingAddress } from "../models/shippingAddress";
import { NewUser } from "../models/newUser";

@Injectable({
providedIn: 'root'
})
export class AuthService {
  
  private url = "user";
  constructor(private http: HttpClient, private router: Router) { }

  Login(loginDTO: LoginDTO): Observable<string> {
    return this.http.post(`${environment.baseApiUrl}/${this.url}/login`, loginDTO, {responseType : 'text'});
  }

  isLoggedIn(): boolean {
    if (!localStorage.getItem('authToken')) {
      return false;
    }
    return true;
  }

  logout(): Observable<JsonResponse> {
    return this.http.post<JsonResponse>(`${environment.baseApiUrl}/${this.url}/logout`, {});
  }

   private secretKey = 'my_secret_key';

   // Function to parse the JWT token
   parseToken(): any {
    var tokenString = localStorage.getItem("authToken");
    if(tokenString != null){
      try {
       return jwtDecode(tokenString);
     } catch (error) {
       console.error('Error parsing token:', error);
       return null;
     }
    }
    
   }
 
   // Function to extract user type from token
   extractUserType(): string | null {
    const tokenData: any = this.parseToken();
    
    // Check if tokenData and role array are defined and not empty
    if (tokenData && Array.isArray(tokenData.role) && tokenData.role.length > 0) {
        // Return the authority value of the first role
        return tokenData.role[0].authority;
    }
    
    return null;
  }

 
   // Example function to extract claims from token
   extractClaims(): any {
     return this.parseToken();
   }

   getUserEmailFromToken(): any {
    const token = localStorage.getItem('authToken');

    if (token) {
      try {
        const payload = token.split('.')[1];
        const decodedPayload = atob(payload);
        const user = JSON.parse(decodedPayload);

        if (user && user.sub) {
          const email = user.sub;
          return email;
        } else {
          console.error('Invalid user payload:', user);
        }
      } catch (error) {
        console.error('Error decoding token payload:', error);
      }
    } else {
      console.error('Token not found.');
    }

    return null;
  }
 
   // Example function to check if the token is expired
   isTokenExpired(): boolean {
    const tokenData: any = this.parseToken();
    if (tokenData && tokenData.exp) {
      const expiryTimestamp = tokenData.exp * 1000; // Convert to milliseconds
      return Date.now() >= expiryTimestamp;
    }
    return true;
   }

   checkTokenOnStartup(): void {
    if (this.isTokenExpired()) {
      localStorage.clear();
      this.router.navigate(['']);
    }
   }

  register(user: NewUser): Observable<JsonResponse> {
    return this.http.post<JsonResponse>(`${environment.baseApiUrl}/${this.url}/registerUser`, user, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'json'
    });
  }

  getUserWishlist(): Observable<Wishlist> {
    return this.http.get<Wishlist>(`${environment.baseApiUrl}/${this.url}/wishlist/${this.getUserEmailFromToken()}`);
  }

  getUserBasket(): Observable<Basket> {
    return this.http.get<Basket>(`${environment.baseApiUrl}/${this.url}/basket/${this.getUserEmailFromToken()}`);
  }

  getUserProfile(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${environment.baseApiUrl}/${this.url}/profile/${this.getUserEmailFromToken()}`);
  }

  updateShippingAddress(newShippingAddress: ShippingAddress): Observable<JsonResponse> {
    return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/updateShippingAddress`, newShippingAddress, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'json'
    });
  }

  createWorker(user: NewUser): Observable<JsonResponse> {
    return this.http.post<JsonResponse>(`${environment.baseApiUrl}/${this.url}/registerWorker`, user, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'json'
    });
  }

  createAdmin(user: NewUser): Observable<JsonResponse> {
    return this.http.post<JsonResponse>(`${environment.baseApiUrl}/${this.url}/registerAdmin`, user, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'json'
    });
  }

  createCourier(user: NewUser): Observable<JsonResponse> {
    return this.http.post<JsonResponse>(`${environment.baseApiUrl}/${this.url}/registerCourier`, user, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'json'
    });
  }

  updateUser(user: UserDTO): Observable<JsonResponse> {
    return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/updateUser`, user, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'json'
    });
  }

}
