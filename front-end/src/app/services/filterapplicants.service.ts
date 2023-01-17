import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import { SkillsAndExperience } from '../model/skills-experience';
import { User } from '../model/user';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', Accept: 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class FilterApplicantService {

  constructor(private http: HttpClient) {}

  filter(jobId: number,userList: Array<number>): Observable <HttpResponse<string>>{
    return this.http.post<string>('https://localhost:8443/filter-applicants/' + jobId.toString(), userList, {observe : 'response'});
  }

}