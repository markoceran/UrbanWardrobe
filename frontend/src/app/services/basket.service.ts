import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { JsonResponse } from "../models/jsonResponse";
import { environment } from "src/environments/environment";
import { Size } from "../models/size";

@Injectable({
    providedIn: 'root'
})
export class BasketService {
      
    private url = "basket";
    constructor(private http: HttpClient) { }

    addBasketItem(productId: number, size: Size): Observable<JsonResponse> {
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/addBasketItem/${productId}`, {size});
    }

    removeBasketItem(basketItemId: number): Observable<JsonResponse> {
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/removeBasketItem/${basketItemId}`, {});
    }

    decreaseQuantityFromBasketItem(basketItemId: number): Observable<JsonResponse> {
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/decreaseQuantityFromBasketItem/${basketItemId}`, {});
    }


}