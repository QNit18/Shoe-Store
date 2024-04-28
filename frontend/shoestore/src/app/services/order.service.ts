import { Injectable } from '@angular/core';
import { OrderDTO } from '../dtos/order/order.dto';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiCreateOrder = `${environment.apiBaseUrl}/orders`;

  constructor(private http: HttpClient) {}

  placeOrder(orderData: OrderDTO): Observable<any> {
    debugger;
    return this.http.post(this.apiCreateOrder, orderData);
  }

  getOrderById(orderId: number): Observable<any> {
    debugger;
    return this.http.get(`${this.apiCreateOrder}/${orderId}`);
  }
}
