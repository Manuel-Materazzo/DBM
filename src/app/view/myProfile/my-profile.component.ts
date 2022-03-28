import {Component} from '@angular/core';
import {TextboxInputType} from '../../util/TextboxInputType';
import {ArtistaService} from '../../services/artista.service';
import {ToastService} from '../../services/toast.service';
import {Artista} from '../../util/DTO/Artista';
import {FileService} from "../../services/file.service";

@Component({
  selector: 'ngx-my-profile',
  styleUrls: ['./my-profile.component.scss'],
  templateUrl: './my-profile.component.html',
})
export class MyProfileComponent {
  constructor(private artstaService: ArtistaService,
              private toastService: ToastService,
              private fileService: FileService) {

    this.refreshDati();

  }

  loading:boolean = true;

  profilo: Artista;
  newProfilo: Artista;

  newImage: File | null = null;


  refreshDati(){
    this.artstaService.getProfilo().subscribe(profilo => {

      //deep clono i dettagli del profilo su due variabili, una modificabile e una no
      this.profilo = {...profilo};
      this.newProfilo = {...profilo};
      this.loading = false;

    }, error => this.toastService.showGenericConnectionErrorToast());
  }

  handleFileInput(files: FileList) {
    this.newImage = files.item(0);
  }

  //controlla la mail e ritorna il nuovo stato della textbox
  CheckEmail():string {
    let re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if(this.newProfilo.email === '' || !re.test(this.newProfilo.email))
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //controlla la bio e ritorna il nuovo stato della textbox
  CheckBio():string {
    if(this.newProfilo.bio === '')
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //controlla il nome e ritorna il nuovo stato della textbox
  CheckName():string {
    if(this.newProfilo.nomeArte === '')
      return TextboxInputType.RED;
    return TextboxInputType.BASIC;
  }

  //esegue tutti i check sui campi e ritorna un boolean
  CheckUserInformations(){
    return this.CheckEmail() === TextboxInputType.BASIC &&
      this.CheckName() === TextboxInputType.BASIC &&
      this.CheckBio()  === TextboxInputType.BASIC;
  }

  //updata il profilo
  SendProfileUpdate() {
    if (!this.CheckUserInformations())
      return;

    this.fileService.uploadImage(this.newImage).subscribe(response =>{
      this.newProfilo.urlAvatar = response.path;

      this.artstaService.updateArtista(this.newProfilo).subscribe(
      res => {
        this.toastService.showGenericSuccessToast();
        this.refreshDati();
      },
        error => this.toastService.showGenericFailToast()
    );
    }, error => {
      console.log(error);
    });

  }
}
