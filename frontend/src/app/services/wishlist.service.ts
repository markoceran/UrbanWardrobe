import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { JsonResponse } from "../models/jsonResponse";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class WishlistService {
      
    private url = "wishlist";
    constructor(private http: HttpClient) { }

    addToWishlist(productId: number): Observable<JsonResponse> {
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/addProduct/${productId}`, {});
    }

    removeFromWishlist(productId: number): Observable<JsonResponse> {
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/removeProduct/${productId}`, {});
    }

}