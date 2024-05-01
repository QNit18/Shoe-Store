import { Injectable } from '@angular/core';
import { OrderDTO } from '../dtos/order/order.dto';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Order } from '../models/order';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiCreateOrder = `${environment.apiBaseUrl}/orders`;
  private apiGetAllOrders = `${environment.apiBaseUrl}/orders/get-orders-by-keyword`;

  constructor(private http: HttpClient) {}

  placeOrder(orderData: OrderDTO): Observable<any> {
    debugger;
    return this.http.post(this.apiCreateOrder, orderData);
  }

  getOrderById(orderId: number): Observable<any> {
    debugger;
    return this.http.get(`${this.apiCreateOrder}/${orderId}`);
  }

  getAllOrders(
    keyword: string,
    page: number,
    limit: number
  ): Observable<Order[]> {
    const params = new HttpParams()
      .set('keyword', keyword.toString())
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get<Order[]>(this.apiGetAllOrders, { params });
  }
}
