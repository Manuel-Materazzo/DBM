import {Inject, Injectable} from "@angular/core";
import {
  NB_AUTH_STRATEGIES,
  NbAuthResult,
  NbAuthService,
  NbAuthStrategy,
  NbAuthToken,
  NbTokenService
} from "@nebular/auth";
import { Observable, of as observableOf } from 'rxjs';
import {map, switchMap} from "rxjs/operators";
import {AuthStrategyService} from "./auth-strategy.service";

@Injectable({ providedIn: 'root' })
export class AuthService extends NbAuthService{

  constructor(protected tokenService: NbTokenService,
              @Inject(NB_AUTH_STRATEGIES) protected strategies, private authStrategy: AuthStrategyService) {
    super(tokenService, strategies);
  }

  authenticate(strategyName: string, data?: any): Observable<NbAuthResult> {
    return this.getStrategy(strategyName).authenticate(data)
      .pipe(
        switchMap((result: NbAuthResult) => {
          return this.processToken(result);
        }),
      );
  }

  //override brutale, non funzionava correttamente
  getStrategy(strategyName): NbAuthStrategy{
    return this.authStrategy;
  }

  private processToken(result: NbAuthResult) {
    if (result.isSuccess() && result.getToken()) {
      return this.tokenService.set(result.getToken())
        .pipe(
          map((token: NbAuthToken) => {
            return result;
          }),
        );
    }

    return observableOf(result);
  }

}
