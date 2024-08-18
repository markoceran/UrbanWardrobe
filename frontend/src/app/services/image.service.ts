import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ImageService {
      
    private url = "image";
    constructor(private http: HttpClient) { }
    
    uploadImages(productCode: string, formData: FormData): Observable<any> {
        return this.http.post<any>(`${environment.baseApiUrl}/${this.url}/upload/${productCode}`, formData);
    }

    getImage(productCode: string, fileName: string): Observable<Blob> {
        return this.http.get(`${environment.baseApiUrl}/${this.url}/download/${productCode}/${fileName}`, {
          responseType: 'blob'
        });
    }
    
}