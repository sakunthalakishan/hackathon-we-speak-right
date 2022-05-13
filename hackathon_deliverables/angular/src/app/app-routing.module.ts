import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AddProfileComponent } from './components/add-profile/add-profile.component';
import { ProfileListComponent} from './components/profile-list/profile-list.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'add-profile' },
  { path: 'add-profile', component: AddProfileComponent },
  { path: 'profiles-list', component: ProfileListComponent },
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
