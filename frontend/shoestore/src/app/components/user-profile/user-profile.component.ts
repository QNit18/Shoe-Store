import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Validator } from 'class-validator';
import { UpdateUserDTO } from 'src/app/dtos/user/update.dto';
import { UserResponse } from 'src/app/responses/user/user.response';
import { TokenService } from 'src/app/services/token.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent implements OnInit {
  userResponse?: UserResponse;
  userProfileForm: FormGroup;
  token: string = '';
  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private tokenService: TokenService
  ) {
    this.userProfileForm = this.formBuilder.group(
      {
        fullname: [''],
        address: ['', [Validators.minLength(3)]],
        password: ['', [Validators.minLength(3)]],
        retype_password: ['', [Validators.minLength(3)]],
        date_of_birth: [Date.now()],
      },
      {
        validators: this.passwordMatchValidator, // Custom validator function for password match
      }
    );
  }

  ngOnInit(): void {
    debugger;
    this.token = this.tokenService.getToken();
    this.userService.getUserDetails(this.token).subscribe({
      next: (response: any) => {
        debugger;
        this.userResponse = {
          ...response,
          date_of_birth: new Date(response.date_of_birth),
        };
        this.userProfileForm.patchValue({
          fullname: this.userResponse?.fullname ?? '',
          address: this.userResponse?.address ?? '',
          date_of_birth: this.userResponse?.date_of_birth
            .toISOString()
            .substring(0, 10),
        });
        this.userService.saveUserResponseToLocalStorage(this.userResponse);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        alert(error.error.message);
      },
    });
  }
  passwordMatchValidator(): ValidatorFn {
    return (formGroup: AbstractControl): ValidationErrors | null => {
      const password = formGroup.get('password')?.value;
      const retypedPassword = formGroup.get('retype_password')?.value;
      if (password !== retypedPassword) {
        return { passwordMismatch: true };
      }

      return null;
    };
  }
  save(): void {
    debugger;
    if (this.userProfileForm.valid) {
      const updateUserDTO: UpdateUserDTO = {
        fullname: this.userProfileForm.get('fullname')?.value,
        address: this.userProfileForm.get('address')?.value,
        password: this.userProfileForm.get('password')?.value,
        retype_password: this.userProfileForm.get('retype_password')?.value,
        date_of_birth: this.userProfileForm.get('date_of_birth')?.value,
      };

      this.userService.updateUserDetail(this.token, updateUserDTO).subscribe({
        next: (response: any) => {
          this.userService.removeUserFromLocalStorage();
          this.tokenService.removeToken();
          this.router.navigate(['/login']);
        },
        error: (error: any) => {
          alert(error.error.message);
        },
      });
    } else {
      if (this.userProfileForm.hasError('passwordMismatch')) {
        alert('Mật khẩu và mật khẩu gõ lại chưa chính xác');
      }
    }
  }
}
