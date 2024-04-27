import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { LoginDTO } from '../../dtos/user/login.dto';
import { UserService } from '../../services/user.service';
import { NgForm } from '@angular/forms';
import {LoginResponse} from '../../responses/user/login.response';
import { TokenService } from 'src/app/services/token.service';
import { RoleService } from 'src/app/services/role.service';
import { Role } from '../../models/role';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;

  // Declare variables
  phoneNumber: string = '11223344';
  password: string = '11223344';

  // Role
  roles: Role[] = [];
  rememberMe: boolean = true;
  selectedRole: Role | undefined;

  constructor(
    private router: Router, 
    private userService: UserService,
    private tokenService : TokenService,
    private roleService : RoleService
  ) {}

  ngOnInit(){
    debugger
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        debugger
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined;
      },
      error: (error : any) => {
        debugger
        console.error("Error Roles: " , error);
      }
    })
  }

  onPhoneNumberChange() {
    console.log(`Phone typed: ` + this.phoneNumber);
  }

  onDateOfBirthChange() {}

  login() {
    const message =
      `Phone: ${this.phoneNumber}  ` + `password: ${this.password}  `;
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
        if (this.rememberMe){
          this.tokenService.setToken(token);
        }

        // this.router.navigate(['/login']);
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
