import { Component, Input, OnInit } from '@angular/core';

import { ViewCell } from 'ng2-smart-table';

@Component({
  template: `
    {{renderValue}}
  `,
})
export class FileRenderComponent implements ViewCell, OnInit {

  renderValue: string;

  @Input() value: any;
  @Input() rowData: any;

  ngOnInit() {
    this.renderValue = this.value

    if(this.value instanceof FileList)
      this.renderValue = this.value[0].name;
  }

}
