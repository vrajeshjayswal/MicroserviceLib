import { IUserDetails } from 'app/shared/model/user-details.model';

export interface IUserAddress {
  id?: number;
  address?: string;
  city?: string;
  state?: string;
  country?: string;
  zipcode?: string;
  isDefault?: boolean;
  addressType?: string;
  userDetails?: IUserDetails;
}

export class UserAddress implements IUserAddress {
  constructor(
    public id?: number,
    public address?: string,
    public city?: string,
    public state?: string,
    public country?: string,
    public zipcode?: string,
    public isDefault?: boolean,
    public addressType?: string,
    public userDetails?: IUserDetails
  ) {
    this.isDefault = this.isDefault || false;
  }
}
