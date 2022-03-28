import {Component, Input, OnInit} from '@angular/core';

import {ViewCell} from 'ng2-smart-table';
import {GeneriUtil} from "../../../util/GeneriUtil";

@Component({
  template: `
    {{renderValue}}
  `,
})
export class GenreRenderComponent implements ViewCell, OnInit {

  renderValue: string;

  @Input() value: any;
  @Input() rowData: any;

  ngOnInit() {
    let genres = this.value;

    if (this.value instanceof Array)
      genres = GeneriUtil.generiListToString(this.value);

    this.renderValue = genres;
  }

}
