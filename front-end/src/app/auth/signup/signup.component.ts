import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { FormControl, Validators, FormGroup } from '@angular/forms';
import {AuthenticationService} from '../../authentication.service';
import { User } from '../../model/user';
import {UserDetails} from '../../model/user-details';
import { MatSnackBar } from '@angular/material/snack-bar'; 

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit {
  user: User = new User();
  loading = false;
  returnUrl: string;
  signuperror: any ;
  dangerBox = false;
  submitattempt = false;
  profilePhoto: File;
  resumeFile: File;
  currentUser: string = "C";

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private _snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    document.body.className = 'selector';
    this.authenticationService.getLoggedInUser().subscribe((userDetails) => {
      if (userDetails) {
        this.router.navigate([this.redirect(userDetails)]);
      }
    });
  }

  redirect(userDetails: UserDetails): string{
    let redirectUrl: string = null;
    if(this.hasRole('PROFESSIONAL', userDetails)) 
      redirectUrl = '/feed';
    else if(this.hasRole('ADMIN', userDetails))
      redirectUrl = '/admin';

    return redirectUrl;
  }

  hasRole(rolename: string , userDetails: UserDetails): boolean{
    let flag = false;
    if(userDetails) {
      userDetails.roles.forEach((role) => {
        if (role === rolename)
          flag = true;
      });
    }
    return flag;
  }

  setProfilePhoto(inputElement){
    this.profilePhoto = inputElement.files[0];
  }
  
  setResumeFile(inputElement){
    this.resumeFile = inputElement.files[0];
  }

  signup(signupForm) {
    if (signupForm.form.valid  && (this.user.password === this.user.passwordConfirm)) {
      this.user.userType = this.currentUser;
      const formWrapper = new FormData();
      
      const userBlob = new Blob([JSON.stringify(this.user)], { type: 'application/json'});
      if (this.profilePhoto) {
        formWrapper.append('imageFile' , this.profilePhoto , 'profilePhoto');
      }

      if (this.resumeFile) {
        if(this.user.userType == 'R'){
          this.resumeFile = null;
        } else{
          formWrapper.append('resumeFile' , this.resumeFile , this.resumeFile.name);
        }
      }
      
      formWrapper.append('object', userBlob );
      this.loading = true;
      this.authenticationService.signup(formWrapper)
        .subscribe(
          response => {
            // const userDetails = new UserDetails();
            // this.user = response.body;
            // userDetails.token = response.headers.get('Authorization');
            // userDetails.id = this.user.id;
            // this.user.roles.forEach( (role) => {
            //   userDetails.roles.push(role.name);
            // } );
            // this.authenticationService.setLoggedInUser(userDetails);
            this._snackBar.open("User created successfully!", '', {duration: 3000,});
            this.router.navigate(['/login']);
          },
          error => {
            this.loading = false;
            this.signuperror = error;
            this.dangerBox = true;
            this.user.roles = [];
            this.submitattempt = true;
          }
        );
    }
    else{
      this.submitattempt = true;
      this.dangerBox = false;
    }
  }
}
