import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginDTO } from '../../dtos/user/login.dto';
import { UserService } from '../../services/user.service';
import { NgForm } from '@angular/forms';
import {LoginResponse} from '../../responses/user/login.response';
import { TokenService } from 'src/app/services/token.service';
import { RoleService } from 'src/app/services/role.service';
import { Role } from '../../models/role';
import { UserResponse } from 'src/app/responses/user/user.response';
import { timestamp } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit{
  @ViewChild('loginForm') loginForm!: NgForm;

  // Declare variables
  phoneNumber: string = '';
  password: string = '';

  // Role
  roles: Role[] = [];
  rememberMe: boolean = true;
  selectedRole: Role | undefined;
  userResponse?: UserResponse;

  constructor(
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService,
    private activatedRouter : ActivatedRoute
  ) {}

  ngOnInit() {
    debugger;
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        debugger;
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined;
      },
      error: (error: any) => {
        debugger;
        console.error('Error Roles: ', error);
      },
    });
  }

  onPhoneNumberChange() {
    console.log(`Phone typed: ` + this.phoneNumber);
  }

  onDateOfBirthChange() {}

  login() {
    const message =
      `Phone: ${this.phoneNumber}  ` + `password: ${this.password}  ` + `role: ${this.roles}`;
    // alert(message);
    debugger;

    const loginDTO: LoginDTO = {
      phone_number: this.phoneNumber,
      password: this.password,
    };

    this.userService.login(loginDTO).subscribe({
      next: (response: LoginResponse) => {
        debugger;
        const { token } = response;

        // Set token in section
        if (this.rememberMe) {
          this.tokenService.setToken(token);
          debugger;
          this.userService.getUserDetails(token).subscribe({
            next: (response: any) => {
              debugger;
              this.userResponse = {
                ...response,
                date_of_birth : new Date(response.date_of_birth),
              };
              this.userService.saveUserResponseToLocalStorage(this.userResponse);
              this.router.navigate(['/']);
            },
            complete: () => {
              debugger;
            },
            error: (error: any) => {
              debugger;
              alert(error?.error?.message);
            },
          });
        }

      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        // Handle if error
        debugger;
        alert(error?.error?.message);
      },
    });
  }
}
