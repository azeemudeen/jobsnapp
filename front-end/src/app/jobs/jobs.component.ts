import { Component, OnInit } from '@angular/core';
import { Job } from '../model/job';
import { User } from '../model/user';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../services/user.service';
import {AuthenticationService} from '../authentication.service';
import {UserDetails} from '../model/user-details';
import { JobsService } from '../services/jobs.service';
import { GlobalConstants } from '../common/GlobalConstants';
import { environment } from 'src/environments/environment';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer } from '@angular/platform-browser';
import { FilterApplicantService } from '../services/filterapplicants.service';

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css'],
  providers: [GlobalConstants]
})
export class JobsComponent implements OnInit {
  user: User = new User();
  userDetails: UserDetails;
  job: Job = new Job();
  jobs: Job[] = new Array<Job>();
  recommendedJobs: Job[] = new Array<Job>();
  sortType: number=0
  currentUser: string = localStorage.getItem('currentUser');
  closeResult = '';
  currentJob: Job;
  constructor(
    private route: ActivatedRoute,
    private router: Router, 
    private userService: UserService, 
    private authService: AuthenticationService,
    private jobService: JobsService,
    private modalService: NgbModal,
    private domSanitizer: DomSanitizer,
    private filterService: FilterApplicantService 
  ) { }

  ngOnInit(): void {
    console.log(this.currentUser);
    this.authService.getLoggedInUser().subscribe((userDetails) => {
      this.userDetails = userDetails;
    });

    this.userService.getUser(this.userDetails.id.toString()).subscribe(
      (user) => {
        Object.assign(this.user , user);
      },
      error => {
        alert(error.message);
      }
    );

    this.jobService.getJobs(this.userDetails.id).subscribe(
      (jobs) => {
        if(this.sortType==0 || this.sortType==null){
          if(this.jobs.length!=0)
          this.jobs = new Array<Job>();
          Object.assign(this.jobs , jobs);
          this.sortJobsByDate();
        }else if(this.sortType==1){
          if(this.jobs.length!=0)
          this.jobs = new Array<Job>();
          Object.assign(this.jobs,this.recommendedJobs);
        }
        console.log(this.jobs);
      },
      error => {
        alert(error.message);
      }
    );
  }

  
  open(content,job: Job) {
    this.currentJob = job;
    console.log(this.currentJob.filtered);
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title',windowClass: 'filterdialog'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  openPdf(user: User){
    if(user.resumeFile != null){
      return this.domSanitizer.bypassSecurityTrustUrl('data:application/pdf;base64,' + user.resumeFile.bytes);
    }
    return '';
  }


  selectedUsers: number[]=new Array<number>();

  addToList(uid){
    if(this.selectedUsers.includes(uid)){
      this.selectedUsers.splice(this.selectedUsers.indexOf(uid),1);
    } else{
      this.selectedUsers.push(uid);
    }
    console.log(this.selectedUsers);
  }

  private getDismissReason(reason: any): string {
    this.selectedUsers.splice(0);
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  changeSort(num:number){
    
    this.sortType = num;
    this.ngOnInit();
  }

  sortJobsByDate() {
    this.jobs.sort(
      (a, b) => {
        return <any>new Date(b.timestamp) - <any>new Date(a.timestamp);
      }
    );
  }


  jobSubmit(jobForm){
    if(jobForm.form.valid){
      this.job.timestamp = new Date();
      this.jobService.addJob(this.job,this.userDetails.id).subscribe(
        responce => {
          this.ngOnInit();
        },
        error => {
          alert(error.message);
        } 
      );
    }
  }

  alreadyApplied(job: Job): boolean{

    for (let u of job.usersApplied) {
      if(u.id == this.userDetails.id)
        return true;
    }
    return false;
 
  } 

  newApplication(jobId: number) {
    this.jobService.apply(jobId,this.userDetails.id).subscribe(
      responce => {
        this.ngOnInit();
      },
      error => {
        alert(error.message);
      } 
    );
  }

  usersJob(job: Job) {
    if(job.userMadeBy.id == this.user.id)
      return true;
    else 
      return false;  
    }

  goToCreateJob(){
    this.router.navigate(['/createjob'])
  }
  clickedfilter: boolean=false;
  filterApplicants(){
    this.clickedfilter = true;
    this.filterService.filter(this.currentJob.id,this.selectedUsers).subscribe(
      responce => {
        this.clickedfilter = false;
        // this.modalService.dismissAll();
        // this.ngOnInit();
        location.reload();
      },
      error => {
        alert(error.message);
      } 
    );
    this.selectedUsers.splice(0);
  }


}
