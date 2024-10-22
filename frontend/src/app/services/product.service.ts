import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { PaginationResponse } from "../models/paginationResponse";
import { Product } from "../models/product";
import { environment } from "src/environments/environment";
import { NewProduct } from "../models/newProduct";
import { JsonResponse } from "../models/jsonResponse";
import { Size } from "../models/size";

@Injectable({
    providedIn: 'root'
})
export class ProductService {
      
    private url = "product";
    constructor(private http: HttpClient) { }
    
    getProducts(page: number): Observable<PaginationResponse> {
        const params = new HttpParams().set('page', page.toString());
        return this.http.get<PaginationResponse>(`${environment.baseApiUrl}/${this.url}/home`, { params });
    }

    getProductsByCategory(page: number, category: string): Observable<PaginationResponse> {
        const params = new HttpParams()
        .set('page', page.toString())
        .set('category', category);
        return this.http.get<PaginationResponse>(`${environment.baseApiUrl}/${this.url}/byCategory`, { params });
    }

    createProduct(product: NewProduct): Observable<Product> {
        return this.http.post<Product>(`${environment.baseApiUrl}/${this.url}/create`, product);
    }


    getProductByCode(code: string): Observable<Product> {
        return this.http.get<Product>(`${environment.baseApiUrl}/${this.url}/${code}`);
    }

    refillQuantity(productId: number, size: string, quantity: number): Observable<JsonResponse> {
        const params = new HttpParams()
        .set('size', size)
        .set('quantity', quantity.toString()); 
        return this.http.put<JsonResponse>(`${environment.baseApiUrl}/${this.url}/refillQuantity/${productId}`, null, {params});
    }
    
    getAllProducts(page: number): Observable<PaginationResponse> {
        const params = new HttpParams().set('page', page.toString());
        return this.http.get<PaginationResponse>(`${environment.baseApiUrl}/${this.url}/allProduct`, { params });
    }

    getAllProductsByCategory(page: number, category: string): Observable<PaginationResponse> {
        const params = new HttpParams()
        .set('page', page.toString())
        .set('category', category);
        return this.http.get<PaginationResponse>(`${environment.baseApiUrl}/${this.url}/allProductsByCategory`, { params });
    }

    searchByCode(code: string): Observable<Product[]> {
        const params = new HttpParams()
        .set('code', code);
        return this.http.get<Product[]>(`${environment.baseApiUrl}/${this.url}/searchByCode`, { params });
    }

    searchAllByCode(code: string): Observable<Product[]> {
        const params = new HttpParams()
        .set('code', code);
        return this.http.get<Product[]>(`${environment.baseApiUrl}/${this.url}/searchAllByCode`, { params });
    }

}
