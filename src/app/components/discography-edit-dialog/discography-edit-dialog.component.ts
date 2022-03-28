import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Brano, BranoApproveDTO} from '../../util/DTO/Brano';
import {NbDialogRef, NbDialogService} from '@nebular/theme';
import {AlbumService} from '../../services/album.service';
import {BraniService} from '../../services/brani.service';
import {ToastService} from '../../services/toast.service';
import {ConfirmDialogComponent} from '../confirm-dialog/confirm-dialog.component';
import {GenreRenderComponent} from '../../view/newAlbum/genreComponent/genre-render.component';
import {GenreEditorComponent} from '../../view/newAlbum/genreComponent/genre-editor.component';
import {TextboxInputType} from '../../util/TextboxInputType';

@Component({
  selector: 'ngx-discography-edit-dialog',
  templateUrl: './discography-edit-dialog.component.html',
  styleUrls: ['./discography-edit-dialog.component.scss']
})
export class DiscographyEditDialogComponent implements OnInit {

  @Input() firstTextLine: string;
  @Input() secondTextLine: string;
  @Input() imageUrl: string;
  @Input() id: number;

  loading: boolean = true;

  brani: Brano[] = [];
  deletedBrani: Brano[] = [];
  editedBrani: Brano[] = [];

  albumName:string;
  albumDescription:string;

  settings = {
    noDataMessage: '',
    actions:{
      edit: true,
      add: false,
      delete: true,
      columnTitle: 'Azioni',
    },
    edit: {
      editButtonContent: '<i class="nb-edit"></i>',
      saveButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      confirmSave: true,
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
    },
  };

  constructor(protected ref: NbDialogRef<DiscographyEditDialogComponent>,
              private albumService: AlbumService,
              private braniService: BraniService,
              private toastService: ToastService,
              private dialogService: NbDialogService) {
  }

  ngOnInit() {

    //riempio gli imput box con i valori default, evito l'assegnazione diretta per facilitare i controlli
    this.albumName = "" + this.firstTextLine;
    this.albumDescription = "" + this.secondTextLine;

    this.braniService.getBraniByIdAlbum(this.id).subscribe(res => {

      res.forEach(brano => {
        brano["generi"] = brano.listGeneri;
        this.brani.push(brano);
      });
      this.loading = false;

    }, (error) => {
      this.toastService.showGenericFailToast();
    });
  }

  dismiss() {
    this.ref.close();
  }

  nameCheck(): string{
    if(this.albumName.length < 3)
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  descriptionCheck(): string{
    if(this.albumDescription.length < 10)
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  onSaveClick() {

    this.deletedBrani.forEach( brano => {
      this.braniService.deleteBranoById(brano.idBrano).subscribe(
        res =>{},
        error => this.toastService.showGenericFailToast()
      );
    });

    this.editedBrani.forEach(brano => {
      brano.listGeneri = brano["generi"];
      this.braniService.updateBrano(brano).subscribe(
        res =>{},
        error => this.toastService.showGenericFailToast()
      );
    });

    if(this.albumName != this.firstTextLine || this.albumDescription != this.secondTextLine){
      this.albumService.updateAlbum({
        idAlbum: this.id,
        descrizione: this.albumDescription,
        nome: this.albumName
      }).subscribe(
        res => this.toastService.showGenericSuccessToast(),
        error => this.toastService.showGenericFailToast()
      );
    }

    this.toastService.showGenericSuccessToast();

  }

  onDeleteAlbumClick(){
    this.dialogService.open(ConfirmDialogComponent, {
      context: {
        title: 'Conferma eliminazione',
        body: 'Sei sicuro di voler eliminare l\'intero album?'
      },
      hasScroll: true
    }).onClose.subscribe(result => {
      this.albumService.deleteAlbumById(this.id).subscribe(
        res => {
          this.toastService.showGenericSuccessToast();
          this.dismiss();
        },
        error => this.toastService.showGenericFailToast()
      );
    } );
  }

  onDeleteConfirm(event): void {

    this.dialogService.open(ConfirmDialogComponent, {
      context: {
        title: 'Conferma eliminazione',
        body: 'Sei sicuro di voler eliminare?'
      },
      hasScroll: true
    }).onClose.subscribe(result => {
      this.deletedBrani.push(event.data);
      if(result)
        event.confirm.resolve();
      event.confirm.reject();
    } );
  }

  onEditConfirm(event): void {
    this.editedBrani.push(event.newData);
    event.confirm.resolve();
  }

}
