import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IUserAddress } from 'app/shared/model/user-address.model';

export interface IUserDetails {
  id?: number;
  type?: string;
  gender?: string;
  birthDate?: Moment;
  phone?: string;
  subscribeToOffer?: boolean;
  passwordChangedOn?: Moment;
  preferredLanguage?: string;
  preferredCurrency?: string;
  user?: IUser;
  userAddresses?: IUserAddress[];
}

export class UserDetails implements IUserDetails {
  constructor(
    public id?: number,
    public type?: string,
    public gender?: string,
    public birthDate?: Moment,
    public phone?: string,
    public subscribeToOffer?: boolean,
    public passwordChangedOn?: Moment,
    public preferredLanguage?: string,
    public preferredCurrency?: string,
    public user?: IUser,
    public userAddresses?: IUserAddress[]
  ) {
    this.subscribeToOffer = this.subscribeToOffer || false;
  }
}
