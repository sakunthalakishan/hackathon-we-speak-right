import { Component, OnInit } from '@angular/core';
import { ProfileService} from '../../shared/profile.service';

@Component({
  selector: 'app-profile-list',
  templateUrl: './profile-list.component.html',
  styleUrls: ['./profile-list.component.css']
})
export class ProfileListComponent implements OnInit {
  ProfileList: any = [];
  url: string;
  searchText: string;

  ngOnInit() {
    this.loadProfiles();
    this.url='https://springboot-wespeakright-speaknow.azuremicroservices.io/api/textToSpeech/';
  }
  constructor(
    public profileService: ProfileService
  ){}
   // Issues list
   loadProfiles() {
    return this.profileService.GetProfiles().subscribe((data: {}) => {
      this.ProfileList = data;
    })
  }
    // Delete issue
    deleteProfile(data){
      const index = this.ProfileList.map(x => {return x.id}).indexOf(data.id);
       return this.profileService.DeleteProfile(data.id).subscribe(res => {
        this.ProfileList.splice(index, 1)
         console.log('Issue deleted!')
       })
    }

}
