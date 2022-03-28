/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { CoreModule } from './@core/core.module';
import { ThemeModule } from './@theme/theme.module';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import {
    NbAccordionModule,
    NbAlertModule,
    NbButton, NbButtonModule,
    NbCardModule,
    NbChatModule, NbCheckboxModule,
    NbDatepickerModule,
    NbDialogModule, NbIconModule, NbInputModule, NbLayoutModule, NbListModule,
    NbMenuModule, NbPopoverModule, NbProgressBarModule, NbRadioModule, NbSelectModule,
    NbSidebarModule, NbSpinnerModule, NbStepperModule,
    NbToastrModule, NbTreeGridModule,
    NbWindowModule,
} from '@nebular/theme';
import {LandingComponent} from './view/landing/landing.component';
import { CardComponent } from './components/card/card.component';
import { SearchbarComponent } from './components/searchbar/searchbar.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { SpinnerComponent } from './components/spinner/spinner.component';
import {ArtistAlbumDialogComponent} from './components/artist-album-dialog/artist-album-dialog.component';
import {AlbumGridComponent} from './components/artist-album-dialog/album-grid/album-grid.component';
import {MyProfileComponent} from './view/myProfile/my-profile.component';
import {NewAlbumComponent} from './view/newAlbum/new-album.component';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {FileEditorComponent} from './view/newAlbum/fileComponent/file-editor.component';
import {FileRenderComponent} from './view/newAlbum/fileComponent/file-render.component';
import {GenreEditorComponent} from './view/newAlbum/genreComponent/genre-editor.component';
import {GenreRenderComponent} from './view/newAlbum/genreComponent/genre-render.component';
import {ConfirmDialogComponent} from './components/confirm-dialog/confirm-dialog.component';
import { LoginComponent } from './view/login/login.component';
import { RegisterComponent } from './view/register/register.component';
import { LogoutComponent } from './view/logout/logout.component';
import { CarouselComponent } from './components/carousel/carousel.component';
import {MyDiscographyComponent} from './view/myDiscography/my-discography.component';
import { ArtistAdministrationComponent } from './view/artist-administration/artist-administration.component';
import { ArtistEditDialogComponent } from './components/artist-edit-dialog/artist-edit-dialog.component';
import { SongApprovationComponent } from './view/song-approvation/song-approvation.component';
import { DiscographyAdministrationComponent } from './view/discography-administration/discography-administration.component';
import { DiscographyEditDialogComponent } from './components/discography-edit-dialog/discography-edit-dialog.component';
import { BanPageComponent } from './view/ban-page/ban-page.component';
import {AuthErrorInterceptorService} from "./services/auth/authError-interceptor.service";
import {AngMusicPlayerModule} from "./components/music-player/ang-music-player.module";
import { DashboardComponent } from './view/dashboard/dashboard.component';
import { FeaturingDialogComponent } from './components/featuring-dialog/featuring-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    CardComponent,
    SearchbarComponent,
    SpinnerComponent,
    ArtistAlbumDialogComponent,
    AlbumGridComponent,
    MyProfileComponent,
    NewAlbumComponent,
    FileEditorComponent,
    FileRenderComponent,
    GenreEditorComponent,
    GenreRenderComponent,
    ConfirmDialogComponent,
    LoginComponent,
    RegisterComponent,
    LogoutComponent,
    CarouselComponent,
    MyDiscographyComponent,
    ArtistAdministrationComponent,
    ArtistEditDialogComponent,
    SongApprovationComponent,
    DiscographyAdministrationComponent,
    DiscographyEditDialogComponent,
    BanPageComponent,
    DashboardComponent,
    FeaturingDialogComponent
  ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        AppRoutingModule,
        NbSidebarModule.forRoot(),
        NbMenuModule.forRoot(),
        NbDatepickerModule.forRoot(),
        NbDialogModule.forRoot(),
        NbWindowModule.forRoot(),
        NbToastrModule.forRoot(),
        NbChatModule.forRoot({
            messageGoogleMapKey: 'AIzaSyA_wNuCzia92MAmdLRzmqitRGvCF7wCZPY',
        }),
        CoreModule.forRoot(),
        ThemeModule.forRoot(),
        NbLayoutModule,
        NbCardModule,
        NbInputModule,
        NbListModule,
        NbButtonModule,
        FormsModule,
        NbSpinnerModule,
        NbTreeGridModule,
        NbIconModule,
        NbSelectModule,
        NbCheckboxModule,
        NbRadioModule,
        NbStepperModule,
        ReactiveFormsModule,
        Ng2SmartTableModule,
        NbPopoverModule,
        NbAlertModule,
        NbAccordionModule,
        AngMusicPlayerModule,
        NbProgressBarModule,
    ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthErrorInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
