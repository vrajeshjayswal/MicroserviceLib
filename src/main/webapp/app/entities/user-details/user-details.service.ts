import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUserDetails } from 'app/shared/model/user-details.model';

type EntityResponseType = HttpResponse<IUserDetails>;
type EntityArrayResponseType = HttpResponse<IUserDetails[]>;

@Injectable({ providedIn: 'root' })
export class UserDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/user-details';

  constructor(protected http: HttpClient) {}

  create(userDetails: IUserDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userDetails);
    return this.http
      .post<IUserDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(userDetails: IUserDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userDetails);
    return this.http
      .put<IUserDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(userDetails: IUserDetails): IUserDetails {
    const copy: IUserDetails = Object.assign({}, userDetails, {
      birthDate: userDetails.birthDate && userDetails.birthDate.isValid() ? userDetails.birthDate.format(DATE_FORMAT) : undefined,
      passwordChangedOn:
        userDetails.passwordChangedOn && userDetails.passwordChangedOn.isValid()
          ? userDetails.passwordChangedOn.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthDate = res.body.birthDate ? moment(res.body.birthDate) : undefined;
      res.body.passwordChangedOn = res.body.passwordChangedOn ? moment(res.body.passwordChangedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((userDetails: IUserDetails) => {
        userDetails.birthDate = userDetails.birthDate ? moment(userDetails.birthDate) : undefined;
        userDetails.passwordChangedOn = userDetails.passwordChangedOn ? moment(userDetails.passwordChangedOn) : undefined;
      });
    }
    return res;
  }
}
