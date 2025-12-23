import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DealsService {

  private API_URL = 'http://localhost:8080/api/deals';

constructor(private http: HttpClient) {}

  getDeals(): Observable<any[]> {
    const token = localStorage.getItem('token');

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<any[]>(this.API_URL, { headers });
  }
  createDeal(payload: any): Observable<any> {
    return this.http.post(this.API_URL, payload);
  }
  updateDeal(dealId: number, payload: any) {
  return this.http.put(
    `http://localhost:8080/api/deals/${dealId}`,
    payload
  );
}
deleteDeal(id: number) {
  return this.http.delete(
    `http://localhost:8080/api/deals/${id}`, 
    { responseType: 'text' }
  );
}




}
