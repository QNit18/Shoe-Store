import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiGetProducts = `${environment.apiBaseUrl}/products`;

  constructor(private http: HttpClient) {}

  getProducts(
    keyword: string,
    selectedCategoryId: number,
    page: number,
    limit: number
  ): Observable<Product[]> {
    const params = new HttpParams()
      .set('keyword', keyword.toString())
      .set('category_id', selectedCategoryId.toString())
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get<Product[]>(this.apiGetProducts, { params });
  }

  getDetailProduct(productId: number) {
    return this.http.get(`${this.apiGetProducts}/${productId}`);
  }

  getProductsByIds(productIds: number[]) :Observable<Product[]>{
    // Move list id to string and inject to params
    debugger;
    const params = new HttpParams().set('ids', productIds.join(','));
    return this.http.get<Product[]>(`${this.apiGetProducts}/by-ids`, {params})
  }
}
