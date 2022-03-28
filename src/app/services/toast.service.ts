import {NbComponentStatus, NbGlobalPhysicalPosition, NbToastrService} from "@nebular/theme";
import {Injectable} from "@angular/core";

@Injectable({providedIn: 'root'})
export class ToastService{

  constructor(private toastrService: NbToastrService) {
  }

  index: number = 1;

  public showToast(type: NbComponentStatus, title: string, body: string) {
    const config = {
      status: type,
      destroyByClick: true,
      duration: 5000,
      hasIcon: true,
      position: NbGlobalPhysicalPosition.BOTTOM_RIGHT,
      preventDuplicates: false,
    };

    this.index += 1;
    this.toastrService.show(body, title, config);
  }

  public showGenericSuccessToast() {
    const config = {
      status: "success",
      destroyByClick: true,
      duration: 5000,
      hasIcon: true,
      position: NbGlobalPhysicalPosition.BOTTOM_RIGHT,
      preventDuplicates: false,
    };

    this.index += 1;
    this.toastrService.show("operazione completata", "Successo!", config);
  }

  public showGenericFailToast() {
    const config = {
      status: "danger",
      destroyByClick: true,
      duration: 5000,
      hasIcon: true,
      position: NbGlobalPhysicalPosition.BOTTOM_RIGHT,
      preventDuplicates: false,
    };

    this.index += 1;
    this.toastrService.show("qualcosa è andato storto", "Oh no...", config);
  }

  public showGenericConnectionErrorToast() {
    const config = {
      status: "warning",
      destroyByClick: true,
      duration: 5000,
      hasIcon: true,
      position: NbGlobalPhysicalPosition.BOTTOM_RIGHT,
      preventDuplicates: false,
    };

    this.index += 1;
    this.toastrService.show("Non riesco a comunicare con il server, riprova più tardi", "Oh no...", config);
  }
}
