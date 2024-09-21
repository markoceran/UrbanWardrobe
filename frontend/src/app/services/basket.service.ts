import { HttpClient, HttpParams } from "@angular/common/http";
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
        const params = new HttpParams()
        .set('productId', productId.toString())
        .set('size', size.toString());
        return this.http.post<JsonResponse>(`${environment.baseApiUrl}/${this.url}/addBasketItem`, {}, {params});
    }

    removeBasketItem(basketItemId: number): Observable<JsonResponse> {
        return this.http.delete<JsonResponse>(`${environment.baseApiUrl}/${this.url}/removeBasketItem/${basketItemId}`, {});
    }

    decreaseQuantityFromBasketItem(basketItemId: number): Observable<JsonResponse> {
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/decreaseQuantityFromBasketItem/${basketItemId}`, {});
    }


}