import { Component, OnInit, NgZone} from '@angular/core';
import { ProfileService } from '../../shared/profile.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import * as RecordRTC from 'recordrtc';
import { DomSanitizer } from '@angular/platform-browser';
/*declare var $: any;*/

@Component({
  selector: 'app-add-profile',
  templateUrl: './add-profile.component.html',
  styleUrls: ['./add-profile.component.css']
})

export class AddProfileComponent implements OnInit {
  success = false;
  custom = false;
  //Service level variables
  profileForm: FormGroup;
  ProfileArr: any = [];
  title = 'micRecorder';
  record;
  recording = false;
  url;
  error;
  id;

  ngOnInit() {
    this.addProfile();
  }
  constructor(
    public fb: FormBuilder,
    private ngZone: NgZone,
    private router: Router,
    public profileService: ProfileService,
    private domSanitizer: DomSanitizer
  ) {}

  addProfile() {
    this.profileForm = this.fb.group({
      firstName: ['',Validators.required],
      lastName: ['',Validators.required],
      preferName: [''],
      audioBase64: ['']
    });
    this.custom = false;
  }
  submitForm() {
    if( localStorage.getItem("audio")!=null){
      this.profileForm.patchValue({
        audioBase64: localStorage.getItem("audio")
      });
    }
    localStorage.clear();
    this.profileService.CreateProfile(this.profileForm.value).subscribe((res) => {
      console.log('Profile added!'+res);
      this.id = res;
      this.success=true;
      this.addProfile();
    });
  }
  //Recording Methods
  sanitize(url: string) {
  return this.domSanitizer.bypassSecurityTrustUrl(url);
  }
  /**
  * Start recording.
  */
  initiateRecording() {
    this.success = false;
    this.url = null;
    this.recording = true;
    localStorage.clear();
    let mediaConstraints = {
    video: false,
    audio: true
    };
    navigator.mediaDevices.getUserMedia(mediaConstraints).then(this.successCallback.bind(this), this.errorCallback.bind(this));
    }
  /**
  * Will be called automatically.
  */
  successCallback(stream) {
    let options = {
    mimeType: "audio/wav",
    numberOfAudioChannels: 1,
    sampleRate: 44100,
  };
  //Start Actuall Recording
  let StereoAudioRecorder = RecordRTC.StereoAudioRecorder;
    this.record = new StereoAudioRecorder(stream, options);
    this.record.record();
  }
  /**
  * Stop recording.
  */
  stopRecording() {
    this.recording = false;
    this.record.stop(this.processRecording.bind(this));
    }
  /**
  * processRecording Do what ever you want with blob
  * @param  {any} blob Blog
  */
  processRecording(blob) {
    this.url = URL.createObjectURL(blob);
    console.log("blob", blob);
    console.log("url", this.url);
    let reader = new FileReader();
    reader.readAsDataURL(blob);
    reader.onloadend = function(){
      let base64String = reader.result.toString();
      let base64Result = base64String.split(',')[1];
      localStorage.setItem("audio",base64Result);
    }
  }
  /**
  * Process Error.
  */
  errorCallback(error) {
    this.error = 'Can not play audio in your browser';
  }

  clear() {
    if (!this.custom) {
      this.url=null;
      this.success = false;
      localStorage.clear();
    }
  }

  getAllProfiles() {
    this.ngZone.run(() => this.router.navigateByUrl('/profiles-list'));
  }
}
