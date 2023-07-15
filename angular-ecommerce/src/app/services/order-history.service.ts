import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OrderHistory } from '../common/order-history';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderHistoryService {

  constructor(private httpClient: HttpClient) { }

  getOrderHistory(email: string): Observable<GetResponsOrderHistory> {
      const orderUrl = environment.luv2shopApiUrl + `/orders/search/findByCustomerEmailOrderByDateCreatedDesc?email=${email}`;
      return this.httpClient.get<GetResponsOrderHistory>(orderUrl);
    }
}

interface GetResponsOrderHistory {
  _embedded: {
    orders: OrderHistory[];
  }
}
