import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { PaginationResponse } from "../models/paginationResponse";
import { Product } from "../models/product";
import { environment } from "src/environments/environment";
import { NewProduct } from "../models/newProduct";

@Injectable({
    providedIn: 'root'
})
export class ProductService {
      
    private url = "product";
    constructor(private http: HttpClient) { }
    
    getProducts(page: number): Observable<PaginationResponse> {
        const params = new HttpParams().set('page', page.toString());
        return this.http.get<PaginationResponse>(`${environment.baseApiUrl}/${this.url}`, { params });
    }

    createProduct(product: NewProduct): Observable<Product> {
        return this.http.post<Product>(`${environment.baseApiUrl}/${this.url}`, product);
    }
    
}
