import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IUserAddress, UserAddress } from 'app/shared/model/user-address.model';
import { UserAddressService } from './user-address.service';
import { IUserDetails } from 'app/shared/model/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/user-details.service';

@Component({
  selector: 'jhi-user-address-update',
  templateUrl: './user-address-update.component.html',
})
export class UserAddressUpdateComponent implements OnInit {
  isSaving = false;
  userdetails: IUserDetails[] = [];

  editForm = this.fb.group({
    id: [],
    address: [],
    city: [],
    state: [],
    country: [],
    zipcode: [],
    isDefault: [],
    addressType: [],
    userDetails: [],
  });

  constructor(
    protected userAddressService: UserAddressService,
    protected userDetailsService: UserDetailsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAddress }) => {
      this.updateForm(userAddress);

      this.userDetailsService.query().subscribe((res: HttpResponse<IUserDetails[]>) => (this.userdetails = res.body || []));
    });
  }

  updateForm(userAddress: IUserAddress): void {
    this.editForm.patchValue({
      id: userAddress.id,
      address: userAddress.address,
      city: userAddress.city,
      state: userAddress.state,
      country: userAddress.country,
      zipcode: userAddress.zipcode,
      isDefault: userAddress.isDefault,
      addressType: userAddress.addressType,
      userDetails: userAddress.userDetails,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAddress = this.createFromForm();
    if (userAddress.id !== undefined) {
      this.subscribeToSaveResponse(this.userAddressService.update(userAddress));
    } else {
      this.subscribeToSaveResponse(this.userAddressService.create(userAddress));
    }
  }

  private createFromForm(): IUserAddress {
    return {
      ...new UserAddress(),
      id: this.editForm.get(['id'])!.value,
      address: this.editForm.get(['address'])!.value,
      city: this.editForm.get(['city'])!.value,
      state: this.editForm.get(['state'])!.value,
      country: this.editForm.get(['country'])!.value,
      zipcode: this.editForm.get(['zipcode'])!.value,
      isDefault: this.editForm.get(['isDefault'])!.value,
      addressType: this.editForm.get(['addressType'])!.value,
      userDetails: this.editForm.get(['userDetails'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAddress>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUserDetails): any {
    return item.id;
  }
}
