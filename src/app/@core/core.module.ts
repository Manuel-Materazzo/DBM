import {ModuleWithProviders, NgModule, Optional, SkipSelf} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NbAuthJWTToken, NbAuthModule, NbAuthOAuth2JWTToken} from '@nebular/auth';
import {NbSecurityModule, NbRoleProvider} from '@nebular/security';
import {of as observableOf} from 'rxjs';

import {throwIfAlreadyLoaded} from './module-import-guard';
import {
  AnalyticsService,
  LayoutService,
  PlayerService,
  SeoService,
  StateService,
} from './utils';
import {AuthStrategyService} from "../services/auth/auth-strategy.service";


export class NbSimpleRoleProvider extends NbRoleProvider {

  getRole() {
    return observableOf(sessionStorage.getItem('role'));
  }
}

export const NB_CORE_PROVIDERS = [
  ...NbAuthModule.forRoot({

    strategies: [
      AuthStrategyService.setup({
        name: 'keycloak',
        token: {
          class: NbAuthOAuth2JWTToken,
          key: 'token'
        },
        baseEndpoint: sessionStorage.getItem('endpoint'),
        refreshToken: {
          endpoint: 'auth/refresh',
          redirect: {
            success: null,
            failure: null,
          },
          defaultErrors: [''],
          defaultMessages: [''],
        },
        login: {
          endpoint: 'auth/login',
          redirect: {
            success: '/pages/dashboard',
            failure: null,
          },
          defaultErrors: ['Combinazione Mail/Password non corretta.'],
          defaultMessages: ['Loggato con successo.'],
        },
        register: {
          endpoint: 'auth/register',
          redirect: {
            success: '/pages/dashboard',
            failure: null,
          },
          defaultErrors: ['Qualcosa è andato storto, riprova piu tardi.'],
          defaultMessages: ['Registrato con successo.'],
        },
        resetPass: {
          endpoint: 'auth/reset-pass',
          redirect: {
            success: null,
            failure: null,
          },
          defaultErrors: ['Qualcosa è andato storto, riprova piu tardi.'],
          defaultMessages: ['La tua nuova password ti è stata spedita via mail.'],
        },
      }),
    ],
  }).providers,

  NbSecurityModule.forRoot({
    accessControl: {
      guest: {
        view: '*',
      },
      user: {
        parent: 'guest',
        create: '*',
        edit: '*',
        remove: '*',
      },
    },
  }).providers,

  {
    provide: NbRoleProvider, useClass: NbSimpleRoleProvider,
  },
  AnalyticsService,
  LayoutService,
  PlayerService,
  SeoService,
  StateService,
];

@NgModule({
  imports: [
    CommonModule,
  ],
  exports: [
    NbAuthModule,
  ],
  declarations: [],
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }

  static forRoot(): ModuleWithProviders<CoreModule> {
    return {
      ngModule: CoreModule,
      providers: [
        ...NB_CORE_PROVIDERS,
      ],
    };
  }
}
