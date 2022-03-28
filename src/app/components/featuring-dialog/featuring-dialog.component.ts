import {Component, Input, OnInit} from '@angular/core';
import {NbDialogRef} from "@nebular/theme";
import {FeaturedService} from "../../services/featured.service";
import {ToastService} from "../../services/toast.service";
import {Featuring} from "../../util/DTO/Featuring";

@Component({
  selector: 'ngx-featuring-dialog',
  templateUrl: './featuring-dialog.component.html',
  styleUrls: ['./featuring-dialog.component.scss']
})
export class FeaturingDialogComponent {

  constructor(
    protected ref: NbDialogRef<FeaturingDialogComponent>,
    private featuriedService: FeaturedService,
    private toastService: ToastService
  ) {
  }

  @Input() artistName: string;
  @Input() idArtista: number;
  dateStart: any;
  dateEnd: any;
  minDate: Date = new Date();
  loading: boolean = false;


  cancel() {
    this.ref.close();
  }

  submit() {
    this.loading = true;
    let featur: Featuring = {
      artisti: {
        idArtista: this.idArtista
      },
      inizio: this.dateStart,
      scadenza: this.dateEnd
    };
    this.featuriedService.createFeaturing(featur).subscribe(res => {
        this.loading = false;
        this.toastService.showGenericSuccessToast()
        this.ref.close();
      }, error => this.toastService.showGenericFailToast()
    );
  }

}
