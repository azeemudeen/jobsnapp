import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../authentication.service';
import { User } from '../model/user';
import { Notification } from '../model/notification';

import { UserDetails } from '../model/user-details';
import { NetworkService } from '../services/network.service';
import { NotificationsService } from '../services/notifications.service';
import { UserNotificationsService } from '../services/usernotification.service';
import { UserNotification } from '../model/usernotification';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  userDetails: UserDetails;
  notifications: Notification[] = new Array<Notification>();
  connRequests: Notification[] = new Array<Notification>();
  postNotifications: Notification[] = new Array<Notification>();
  userNotifications: UserNotification[] = new Array<UserNotification>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private authenticationService: AuthenticationService,
    private notificationService: NotificationsService,
    private domSanitizer: DomSanitizer,
    private usernotificationService: UserNotificationsService
  ) { }

  ngOnInit(): void {
    this.authenticationService.getLoggedInUser().subscribe((userDetails) => {
      this.userDetails = userDetails;
    });

    this.usernotificationService.getNotifications(this.userDetails.id).subscribe(
      (userNotifications) => {
        Object.assign(this.userNotifications , userNotifications);
      }
    );

    this.notificationService.getNotifications(this.userDetails.id).subscribe(
      (notifications) => {
        Object.assign(this.notifications , notifications);
        this.notifications.forEach(
          (notif) => {
            if(notif.type === 'CONNECTION_REQUEST') {
              this.connRequests.push(notif);
            } else if (notif.type === 'COMMENT' || notif.type === 'INTEREST'){
              this.postNotifications.push(notif);
            } 
          }
        );
      }
    );

  }

  getType(type: string): Number {
    // alert(type);
    if(type === 'CONNECTION_REQUEST')
      return 0;
    else if(type === 'COMMENT')
      return 1;
    else
      return 2;
  }


  filterNotifications() {
    this.notifications.forEach(
      (notif) => {
        if(notif.type === 'CONNECTION_REQUEST') {
           this.connRequests.push(notif);
        } else if (notif.type === 'COMMENT' || notif.type === 'INTEREST'){
          this.postNotifications.push(notif);
        } 
      }
    );
  }

  acceptConnection(conId: number) {
    
    this.notificationService.acceptConnection(this.userDetails.id,conId).subscribe(
      responce => {},
      error => {
        alert(error.message);
      }      
    );
    location.reload();
  }

  goToProfile(user: User) {
    this.router.navigate(['/users/' + user.id.toString()]).then(() => {
      location.reload();
    });   
  }

  displayProfilePhoto(user: User): any{
    if(user.profilePicture) {
      if (user.profilePicture.type === 'image/png')
        return this.domSanitizer.bypassSecurityTrustUrl('data:image/png;base64,' + user.profilePicture.bytes);
      else if (user.profilePicture.type === 'image/jpeg')
        return this.domSanitizer.bypassSecurityTrustUrl('data:image/jpeg;base64,' + user.profilePicture.bytes);
    }
    return null;
  }

}
