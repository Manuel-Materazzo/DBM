import {Component, Input} from '@angular/core';
import { NbDialogRef } from '@nebular/theme';

@Component({
  selector: 'ngx-confirm-dialog',
  templateUrl: 'confirm-dialog.component.html',
  styleUrls: ['confirm-dialog.component.scss'],
})
export class ConfirmDialogComponent {

  constructor(protected ref: NbDialogRef<ConfirmDialogComponent>) {}
  @Input() title: string;
  @Input() body: string;


  cancel() {
    this.ref.close(false);
  }

  submit() {
    this.ref.close(true);
  }
}
