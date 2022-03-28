import {Component} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {TextboxInputType} from '../../util/TextboxInputType';
import {FileEditorComponent} from './fileComponent/file-editor.component';
import {Router} from '@angular/router';
import {FileRenderComponent} from './fileComponent/file-render.component';
import {GenreRenderComponent} from './genreComponent/genre-render.component';
import {GenreEditorComponent} from './genreComponent/genre-editor.component';
import {NbDialogService} from '@nebular/theme';
import {ConfirmDialogComponent} from '../../components/confirm-dialog/confirm-dialog.component';
import {NbRoleProvider} from '@nebular/security';
import {AlbumService} from '../../services/album.service';
import {Album} from '../../util/DTO/Album';
import {ToastService} from '../../services/toast.service';
import {Brano} from '../../util/DTO/Brano';
import {AuthService} from "../../services/auth/auth.service";
import {FileService} from "../../services/file.service";

@Component({
  selector: 'ngx-new-album',
  templateUrl: 'new-album.component.html',
  styleUrls: ['new-album.component.scss'],
})
export class NewAlbumComponent {

  name: string = '';
  image: File = null;
  imageSrc: any;
  descrizione: string = '';
  dataPubblicazione: any;
  canzoni: any[] = [];
  idArtista: string = '';
  admin: boolean = false;

  submitted: boolean = false;
  submitError: boolean = false;
  done: boolean = false;

  minDate: Date = new Date();

  settings = {
    noDataMessage: '',
    actions: {
      edit: false,
      add: true,
      delete: true,
      columnTitle: 'Azioni',
    },
    add: {
      addButtonContent: '<i class="nb-plus"></i>',
      createButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      confirmCreate: true,
    },
    delete: {
      deleteButtonContent: '<i class="nb-trash"></i>',
      confirmDelete: true,
    },
    columns: {
      titolo: {
        title: 'Titolo Canzone',
        type: 'string',
        filter: false
      },
      generi: {
        title: 'Generi',
        filter: false,
        type: 'custom',
        renderComponent: GenreRenderComponent,
        editor: {
          type: 'custom',
          component: GenreEditorComponent,
        },
      },
      file: {
        title: 'File Audio',
        filter: false,
        type: 'custom',
        editable: false,
        renderComponent: FileRenderComponent,
        editor: {
          type: 'custom',
          component: FileEditorComponent,
        },
      },
    },
  };

  constructor(private fb: FormBuilder,
              private router: Router,
              private dialogService: NbDialogService,
              private authService: AuthService,
              private albumService: AlbumService,
              private roleProvider: NbRoleProvider,
              private toastService: ToastService,
              private fileService: FileService) {

    roleProvider.getRole().subscribe( role => this.admin = (role == "admin"));

  }

  //controlla il nome e ritorna il nuovo stato della textbox
  CheckName(): string {
    if (this.name?.length < 3)
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //l'immagine e ritorna il nuovo stato della textbox
  CheckImage(): string {
    if (this.image === null)
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //controlla il nome e ritorna il nuovo stato della textbox
  CheckDescription(): string {
    if (this.descrizione?.length < 20)
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //controlla la data di pubblicazione e ritorna il nuovo stato della textbox
  CheckPubblicationDate(): string {
    if (this.dataPubblicazione === undefined)
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //boolean wrapper, controlla la validità del primo step
  firstStepValid(): boolean {
    return this.CheckName() === TextboxInputType.BASIC && this.CheckImage() === TextboxInputType.BASIC;
  }

  //boolean wrapper, controlla la validità del secondo step
  secondStepValid(): boolean {
    return this.CheckDescription() === TextboxInputType.BASIC
  }

  //boolean wrapper, controlla la validità del terzo step
  thirdStepValid(): boolean {
    return this.CheckPubblicationDate() === TextboxInputType.BASIC
  }

  //controlla la validità del quarto step
  forthStepValid(): boolean {
    return this.canzoni?.length > 0;
  }


  handleImageInput(files: FileList) {
    this.image = files.item(0);

    const reader = new FileReader();
    reader.onload = e => this.imageSrc = reader.result;

    reader.readAsDataURL(this.image);
  }

  onLastSubmit() {
    //failsafe se l'utente manda 2 volte il form
    if (this.submitted) return;

    let brani: Brano[] = [];

    this.canzoni.forEach(canzone => {

      this.fileService.uploadAudio(canzone.file[0]).subscribe(response =>{
        brani.push({
          durata: response.length,
          titolo: canzone.titolo,
          url: response.path,
          listGeneri: canzone.generi

        });

        this.fileService.uploadImage(this.image).subscribe(response =>{
          let album: Album = {
            casaDiscografica: 'this',
            dataPubblicazione: this.dataPubblicazione,
            descrizione: this.descrizione,
            nome: this.name,
            urlCopertina: response.path,
            braniList: brani
          };

          //l'admin può pubblicare al posto degli altri
          if (this.admin) album.listArtisti = [{idArtista: +this.idArtista}]

          this.submitted = true;
          this.submitError = false;
          this.done = false;
          this.settings.actions.add = false;
          this.settings.actions.delete = false;

          this.albumService.createAlbum(album).subscribe(
            res => {
              this.done = true;
              this.toastService.showGenericSuccessToast();
            },
            error => {
              this.toastService.showGenericFailToast();
              //permetto di nuovo la modifica in caso di errori
              this.submitted = false;
              this.submitError = true;
              this.settings.actions.add = true;
              this.settings.actions.delete = true;
            }
          );
        });

      }, error => this.toastService.showGenericFailToast());
    });


  }

  onDeleteConfirm(event): void {

    this.dialogService.open(ConfirmDialogComponent, {
      context: {
        title: 'Conferma eliminazione',
        body: 'Sei sicuro di voler eliminare?'
      },
      hasScroll: true
    }).onClose.subscribe(result => {
      if (!result)
        event.confirm.reject();

      event.confirm.resolve();
    });
  }

  onCreateConfirm(event): void {
    //validazione campi vuoti
    if (!(event.newData.titolo.length >= 3 && event.newData.generi.length > 0 && event.newData.file))
      event.confirm.reject();

    event.confirm.resolve();
  }

  gotoAlbumList() {
    this.router.navigate(['/pages/mydiscography']);
  }

  gotoNewAlbum() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['/pages/newalbum']);
  }

}
