import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { JsonResponse } from "../models/jsonResponse";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class OrderService {
      
    private url = "order";
    constructor(private http: HttpClient) { }

    create(): Observable<JsonResponse> {
        return this.http.post<JsonResponse>(`${environment.baseApiUrl}/${this.url}/create`, {});
    }

    getById(orderId: number): Observable<any> {
        return this.http.get<any>(`${environment.baseApiUrl}/${this.url}/${orderId}`);
    }

}