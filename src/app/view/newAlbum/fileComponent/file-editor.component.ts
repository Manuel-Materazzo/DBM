import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import {DefaultEditor} from 'ng2-smart-table';

@Component({
  template: `
    <input [ngClass]="inputClass"
            #url
            accept="audio/*" type="file"
            class="form-control short-input"
            [name]="cell.getId()"
            [disabled]="!cell.isEditable()"
            [placeholder]="cell.getTitle()"
            (click)="onClick.emit($event)"
            (change)="updateValue()"
            (keydown.enter)="onEdited.emit($event)"
            (keydown.esc)="onStopEditing.emit()">
  `,
})
export class FileEditorComponent extends DefaultEditor implements AfterViewInit {

  @ViewChild('url') url: ElementRef;

  constructor() {
    super();
  }


  ngAfterViewInit() {
    if (this.cell.newValue !== '') {
      this.url.nativeElement.files = this.cell.newValue;
    }
  }

  updateValue() {
    this.cell.newValue = this.url.nativeElement.files;
  }


}
