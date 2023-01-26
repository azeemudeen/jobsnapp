import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/internal/Observable";

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json', Accept: 'application/json' })
};

@Injectable({
    providedIn: 'root'
})

export class UserNotificationsService {

    constructor(private http: HttpClient) {}
  
    getNotifications(userId: number): Observable<string>{
      return this.http.get<string>('https://localhost:8443/notification/' + userId.toString());
    }
}