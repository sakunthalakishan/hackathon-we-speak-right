import { Component, OnInit } from '@angular/core';
import { ProfileService} from '../../shared/profile.service';
import { FormGroup, FormControl, Validators} from '@angular/forms';

@Component({
  selector: 'app-profile-list',
  templateUrl: './profile-list.component.html',
  styleUrls: ['./profile-list.component.css']
})
export class ProfileListComponent implements OnInit {
  ProfileList: any = [];
  url: string;
  searchText: string;
  selectedCountry: any;
  selectedSpeed: any;
  countryMap = new Map();
  country: string;
  bindedCountryKeys: any =[];
  speedList = new Array();
  speed: string;

    form = new FormGroup({
      selectedCountry: new FormControl('USA', Validators.required),
      selectedSpeed: new FormControl('USA', Validators.required)
    });

  ngOnInit() {
    this.loadProfiles();
    this.url='https://springboot-wespeakright-speaknow.azuremicroservices.io/api/textToSpeech/';
    this.selectedCountry='USA';
    this.selectedSpeed='default';
    this.country='USA';
    this. countryMap.set('Australia','AU');
    this. countryMap.set('Canada','CA');
    this.countryMap.set('UK','GB');
    this.countryMap.set('Hongkong','HK');
    this.countryMap.set('Ireland','IE');
    this.countryMap.set('India','IN');
    this.countryMap.set('Kenya','KE');
    this.countryMap.set('Nigeria','NG');
    this.countryMap.set('New Zealand','NZ');
    this.countryMap.set('Singapore','SG');
    this.countryMap.set('USA','US');
    this.bindedCountryKeys = Array.from(this.countryMap.keys());
    this.speedList =['x-slow', 'slow','medium', 'fast','x-fast','default'];
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

  get f(){
    return this.form.controls;
  }
  changeCountry(e) {
    console.log(e.target.value);
    this.country = this.countryMap.get(e.target.value);
  }

  clickMethod(data,name: string) {
    if(confirm("Are you sure to delete "+name)) {
      this.deleteProfile(data);
    }
  }
}
