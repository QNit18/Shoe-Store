import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { RegisterDTO } from '../../dtos/user/register.dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;

  // Declare variables
  phoneNumber: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth: Date;

  constructor(private router: Router, private userService: UserService) {
    this.phoneNumber = '';
    this.password = '';
    this.retypePassword = '';
    this.fullName = '';
    this.address = '';
    this.isAccepted = false;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18); // Required to be over 18 years old
    // Injection
  }

  onPhoneNumberChange() {
    console.log(`Phone typed: ` + this.phoneNumber);
  }

  onDateOfBirthChange() {}

  register() {
    const message =
      `Phone: ${this.phoneNumber}  ` +
      `password: ${this.password}  ` +
      `retypePassword: ${this.retypePassword} ` +
      `fullName: ${this.fullName}  ` +
      `address: ${this.fullName}  ` +
      `isAccepted: ${this.isAccepted}  ` +
      `dateOfBirth: ${this.dateOfBirth}`;
    // alert(message);
    debugger;
    const registerDTO: RegisterDTO = {
      fullname: this.fullName,
      phone_number: this.phoneNumber,
      address: this.address,
      password: this.password,
      retype_password: this.retypePassword,
      date_of_birth: this.dateOfBirth,
      facebook_account_id: 0,
      google_account_id: 0,
      role_id: 2,
    };
    this.userService.register(registerDTO).subscribe({
      next: (response: any) => {
        debugger;
        this.router.navigate(['/login']);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        // Handle if error
        debugger;
        alert(`Cannot register, error: ${error.error}`);
        console.error('Đăng ký không thành công:', error);
      },
    });
  }

  // How to check password match?
  checkPasswordMatch() {
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({
        passwordMismatch: true,
      });
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }

  checkAge() {
    if (this.dateOfBirth) {
      const today = new Date();
      const birthDate = new Date(this.dateOfBirth);
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiffirence = today.getMonth() - birthDate.getMonth();

      if (
        monthDiffirence < 0 ||
        (monthDiffirence === 0 && today.getDate() < birthDate.getDate())
      ) {
        age--;
      }

      if (age < 18) {
        this.registerForm.form.controls['dateOfBirth'].setErrors({
          invalidAge: true,
        });
      } else {
        this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      }
    }
  }
}
