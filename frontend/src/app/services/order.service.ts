import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { JsonResponse } from "../models/jsonResponse";
import { environment } from "src/environments/environment";
import { PaginationResponse } from "../models/paginationResponse";
import { Order } from "../models/order";

@Injectable({
    providedIn: 'root'
})
export class OrderService {
      
    private url = "order";
    constructor(private http: HttpClient) { }

    create(): Observable<JsonResponse> {
        return this.http.post<JsonResponse>(`${environment.baseApiUrl}/${this.url}/create`, {});
    }

    getByCode(code: string): Observable<Order> {
        return this.http.get<Order>(`${environment.baseApiUrl}/${this.url}/${code}`);
    }

    cancel(orderId: number): Observable<JsonResponse> {
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/cancelOrder/${orderId}`, {});
    }

    getSentOrders(page: number): Observable<PaginationResponse> {
        const params = new HttpParams().set('page', page.toString());
        return this.http.get<PaginationResponse>(`${environment.baseApiUrl}/${this.url}/sentOrders`, { params });
    }

    getPendingOrders(page: number): Observable<PaginationResponse> {
        const params = new HttpParams().set('page', page.toString());
        return this.http.get<PaginationResponse>(`${environment.baseApiUrl}/${this.url}/pendingOrders`, { params });
    }

    getDeliveredOrders(page: number): Observable<PaginationResponse> {
        const params = new HttpParams().set('page', page.toString());
        return this.http.get<PaginationResponse>(`${environment.baseApiUrl}/${this.url}/deliveredOrders`, { params });
    }

    sent(orderId: number): Observable<JsonResponse> {
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/sentOrder/${orderId}`, {});
    }

    deliver(orderId: number): Observable<JsonResponse> {
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/deliverOrder/${orderId}`, {});
    }

}