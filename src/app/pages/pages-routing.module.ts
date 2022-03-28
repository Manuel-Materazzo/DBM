import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PagesComponent } from './pages.component';
import {MyProfileComponent} from '../view/myProfile/my-profile.component';
import {NewAlbumComponent} from '../view/newAlbum/new-album.component';
import {MyDiscographyComponent} from '../view/myDiscography/my-discography.component';
import {ArtistAdministrationComponent} from "../view/artist-administration/artist-administration.component";
import {SongApprovationComponent} from '../view/song-approvation/song-approvation.component';
import {
  DiscographyAdministrationComponent
} from '../view/discography-administration/discography-administration.component';
import {AuthGuard, AuthGuardAdmin} from "../services/auth/auth-guard.service";
import {DashboardComponent} from "../view/dashboard/dashboard.component";

const routes: Routes = [{
  path: '',
  component: PagesComponent,
  children: [
    {
      path: 'dashboard',
      component: DashboardComponent,
      canActivate: [AuthGuard]
    },
    {
      path: 'profile',
      component: MyProfileComponent,
      canActivate: [AuthGuard]
    },
    {
      path: 'mydiscography',
      component: MyDiscographyComponent,
      canActivate: [AuthGuard]
    },
    {
      path: 'newalbum',
      component: NewAlbumComponent,
      canActivate: [AuthGuard]
    },
    {
      path: 'artistadministration',
      component: ArtistAdministrationComponent,
      canActivate: [AuthGuardAdmin]
    },
    {
      path: 'discographyadministration',
      component: DiscographyAdministrationComponent,
      canActivate: [AuthGuardAdmin]
    },
    {
      path: 'approvesongs',
      component: SongApprovationComponent,
      canActivate: [AuthGuardAdmin]
    },
    {
      path: '',
      redirectTo: 'dashboard',
      pathMatch: 'full',
    }
  ],
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {
}
