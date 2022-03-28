import {Component, Input, OnInit} from '@angular/core';
import {NbDialogRef, NbDialogService} from "@nebular/theme";
import {TextboxInputType} from "../../util/TextboxInputType";
import {ArtistaService} from "../../services/artista.service";
import {Artista} from "../../util/DTO/Artista";
import {ToastService} from "../../services/toast.service";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {FileService} from "../../services/file.service";
import {FeaturingDialogComponent} from "../featuring-dialog/featuring-dialog.component";

@Component({
  selector: 'ngx-artist-edit-dialog',
  templateUrl: './artist-edit-dialog.component.html',
  styleUrls: ['./artist-edit-dialog.component.scss']
})
export class ArtistEditDialogComponent implements OnInit {

  constructor(protected ref: NbDialogRef<ArtistEditDialogComponent>,
              private artistaService: ArtistaService,
              private toastService: ToastService,
              private dialogService: NbDialogService,
              private fileService: FileService) {

  }

  @Input() id: number;

  imageUrl: string;
  artistName: string;
  newImage: File | null = null;
  newArtistName: string = '';
  newEmail: string = '';
  newBio: string = '';
  bannato: boolean = false;

  loading: boolean = true;

  ngOnInit(): void {
    this.artistaService.getArtistaById(this.id).subscribe(res => {

      this.newBio = res.bio;
      this.newEmail = res.email;
      this.newArtistName = res.nomeArte;
      this.artistName = res.nomeArte;
      this.imageUrl = res.urlAvatar;
      this.bannato = res.bannato;
      this.loading = false;
    }, (error) => {
      this.toastService.showGenericFailToast();
      this.loading = false;
      this.dismiss();
    });
  }

  dismiss() {
    this.ref.close();
  }

  handleFileInput(files: FileList) {
    this.newImage = files.item(0);
  }

  //controlla la mail e ritorna il nuovo stato della textbox
  CheckEmail(): string {
    let re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (this.newEmail === '' || !re.test(this.newEmail))
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //controlla la bio e ritorna il nuovo stato della textbox
  CheckBio(): string {
    if (this.newBio === '')
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //controlla il nome e ritorna il nuovo stato della textbox
  CheckName(): string {
    if (this.newArtistName === '')
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //esegue tutti i check sui campi e ritorna un boolean
  CheckUserInformations() {
    return this.CheckEmail() === TextboxInputType.BASIC &&
      this.CheckName() === TextboxInputType.BASIC &&
      this.CheckBio() === TextboxInputType.BASIC;
  }

  //updata il profilo
  SendProfileUpdate() {
    if (!this.CheckUserInformations())
      return;

    this.loading = true;

    if (this.newImage != null) {
      this.fileService.uploadImage(this.newImage).subscribe(response => {

        let artista: Artista = {
          bannato: this.bannato,
          bio: this.newBio,
          email: this.newEmail,
          idArtista: this.id,
          nomeArte: this.newArtistName,
          urlAvatar: response.path
        };

        this.artistaService.updateArtista(artista).subscribe(() => {
          this.loading = false;
          this.toastService.showGenericSuccessToast();
          this.dismiss();
        }, (error) => {
          this.toastService.showGenericFailToast();
          this.loading = false;
        });

      });
    } else {

      let artista: Artista = {
        bannato: this.bannato,
        bio: this.newBio,
        email: this.newEmail,
        idArtista: this.id,
        nomeArte: this.newArtistName
      };

      this.artistaService.updateArtista(artista).subscribe(() => {
        this.loading = false;
        this.toastService.showGenericSuccessToast();
        this.dismiss();
      }, (error) => {
        this.toastService.showGenericFailToast();
        this.loading = false;
      });

    }

  }

  //banna il profilo
  BanHammer() {

    this.dialogService.open(ConfirmDialogComponent, {
      context: {
        title: 'Conferma Ban',
        body: 'Sei sicuro di voler bannare ' + this.artistName + ' ?'
      },
      hasScroll: true
    }).onClose.subscribe(result => {
      if (result) {
        this.bannato = true;
        this.SendProfileUpdate();
      }
    });
  }

  //sbanna il profilo
  UnBanHammer() {

    this.bannato = false;
    this.SendProfileUpdate();

  }

  //apro la finestra featured
  FeatureHammer(){
    this.dialogService.open(FeaturingDialogComponent, {
      context: {
        artistName: this.artistName,
        idArtista: this.id
      },
      hasScroll: true
    })
  }

  //elimina il profilo
  SendProfileDelete() {
    this.dialogService.open(ConfirmDialogComponent, {
      context: {
        title: 'Conferma Ban',
        body: 'Sei sicuro di voler eliminare il profilo di ' + this.artistName + ' ? Quest\'t azione è irreversibile'
      },
      hasScroll: true
    }).onClose.subscribe(result => {
      if (result) {
        this.artistaService.deleteArtistaById(this.id).subscribe(() => {
          this.toastService.showGenericSuccessToast();
          this.dismiss();
        }, (error) => {
          this.toastService.showGenericFailToast();
        });
      }
    });
  }
}
