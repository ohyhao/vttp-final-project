import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, lastValueFrom } from "rxjs";
import { AppUser, Credentials, Registration } from "../models";


const httpHeaders = {
    headers: new HttpHeaders()
        .set('Content-Type', 'application/json')
        .set('Accept', 'application/json')
}

@Injectable()
export class UserService {

    constructor(private http: HttpClient) {}

    authenticateUser(credentials: Credentials): Promise<AppUser> {

        return lastValueFrom(
            this.http.post<AppUser>('/api/authenticate', credentials, httpHeaders)
            )
    }

    registerUser(reg: Registration): Promise<any> {
        
        return lastValueFrom(
            this.http.post<string>('/api/register', reg, httpHeaders)
        )
    }

    logout(): Promise<any> {

        return lastValueFrom(
            this.http.post('/api/logout', {}, httpHeaders)
        )
    }

    upload(formData: FormData, username: string): Promise<any> {

        return lastValueFrom(
            this.http.post(`/api/user/profile-pic/upload/${username}`, formData)
        )
    }

    getUserDetails(username: string): Promise<AppUser> {

        return lastValueFrom(
            this.http.get<AppUser>(`/api/user/${username}`, httpHeaders)
        )
    }

    
}