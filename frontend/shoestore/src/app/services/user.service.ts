import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/user/register.dto';
import { LoginDTO } from '../dtos/user/login.dto';
import { environment } from '../environments/environment';
import { UserResponse } from '../responses/user/user.response';
import { UpdateUserDTO } from '../dtos/user/update.dto';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiRegister = `${environment.apiBaseUrl}/users/register`;
  private apiLogin = `${environment.apiBaseUrl}/users/login`;
  private apiUserDetail = `${environment.apiBaseUrl}/users/details`;

  private apiConfig = {
    headers: this.createHeaders(),
  };

  constructor(private http: HttpClient) {}

  private createHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-type': 'application/json',
      'Accept-Language': 'en',
    });
  }

  register(registerDTO: RegisterDTO): Observable<any> {
    return this.http.post(this.apiRegister, registerDTO, this.apiConfig);
  }

  login(loginDTO: LoginDTO): Observable<any> {
    return this.http.post(this.apiLogin, loginDTO, this.apiConfig);
  }

  getUserDetails(token: string) {
    return this.http.post(this.apiUserDetail, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      }),
    });
  }

  saveUserResponseToLocalStorage(userResponse?: UserResponse) {
    try {
      if (userResponse == null || !userResponse) {
        debugger;
        return;
      }
      const userResponseJson = JSON.stringify(userResponse);
      localStorage.setItem('user', userResponseJson);
      console.log('User response saved to local storage.');
    } catch (error) {
      console.log('Error saving user response to local storage:', error);
    }
  }

  getUserResponseFromLocalStorage(): UserResponse | null {
    try {
      const userResponseJson = localStorage.getItem('user');

      if (userResponseJson == null || userResponseJson == undefined) {
        return null;
      }

      const userResponse = JSON.parse(userResponseJson!);

      console.log('User response get from local storage.');
      return userResponse;
    } catch (error) {
      console.log('Error saving user response to local storage:', error);
      return null;
    }
  }

  removeUserFromLocalStorage(): void {
    try {
      // Remove the user data from local storage using the key
      localStorage.removeItem('user');
      console.log('User data removed from local storage.');
    } catch (error) {
      console.error('Error removing user data from local storage:', error);
      // Handle the error as needed
    }
  }

  updateUserDetail(token: string, updateUserDTO: UpdateUserDTO) {
    debugger;
    let userResponse = this.getUserResponseFromLocalStorage();
    return this.http.put(
      `${this.apiUserDetail}/${userResponse?.id}`,
      updateUserDTO,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        }),
      }
    );
  }
}
