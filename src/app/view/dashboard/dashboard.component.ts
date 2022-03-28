import {Component, OnInit} from '@angular/core';
import {NbRoleProvider} from "@nebular/security";
import {StatsService} from "../../services/stats.service";
import {ToastService} from "../../services/toast.service";

@Component({
  selector: 'ngx-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  constructor(private roleProvider: NbRoleProvider,
              private statService: StatsService,
              private toastService: ToastService) {

    roleProvider.getRole().subscribe(role => this.isAdmin = role === "admin");

    statService.getStats().subscribe(stats => {
        this.progressInfoData = [
          {
            title: 'Artisti Registrati',
            value: stats.totaleArtisti,
            activeProgress: 100,
            description: 'Più dell\'anno scorso (100%)',
          },
          {
            title: 'Album Creati',
            value: stats.totaleALbum,
            activeProgress: 100,
            description: 'Più dell\'anno scorso (100%)',
          },
          {
            title: 'Brani Caricati',
            value: stats.totaleBrani,
            activeProgress: 100,
            description: 'Più dell\'anno scorso (100%)',
          },
          {
            title: 'Generi musicali',
            value: stats.totaleGeneri,
            activeProgress: 100,
            description: 'Più dell\'anno scorso (100%)',
          },
        ];
        this.loading = false;
      },
      error => this.toastService.showGenericConnectionErrorToast()
    );

  }

  isAdmin: boolean = false;
  loading: boolean = true;

  progressInfoData = [];


}
