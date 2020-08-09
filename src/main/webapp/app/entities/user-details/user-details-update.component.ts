import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IUserDetails, UserDetails } from 'app/shared/model/user-details.model';
import { UserDetailsService } from './user-details.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-user-details-update',
  templateUrl: './user-details-update.component.html',
})
export class UserDetailsUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  birthDateDp: any;
  passwordChangedOnDp: any;

  editForm = this.fb.group({
    id: [],
    type: [],
    gender: [],
    birthDate: [],
    phone: [],
    subscribeToOffer: [],
    passwordChangedOn: [],
    preferredLanguage: [],
    preferredCurrency: [],
    user: [],
  });

  constructor(
    protected userDetailsService: UserDetailsService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userDetails }) => {
      this.updateForm(userDetails);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(userDetails: IUserDetails): void {
    this.editForm.patchValue({
      id: userDetails.id,
      type: userDetails.type,
      gender: userDetails.gender,
      birthDate: userDetails.birthDate,
      phone: userDetails.phone,
      subscribeToOffer: userDetails.subscribeToOffer,
      passwordChangedOn: userDetails.passwordChangedOn,
      preferredLanguage: userDetails.preferredLanguage,
      preferredCurrency: userDetails.preferredCurrency,
      user: userDetails.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userDetails = this.createFromForm();
    if (userDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.userDetailsService.update(userDetails));
    } else {
      this.subscribeToSaveResponse(this.userDetailsService.create(userDetails));
    }
  }

  private createFromForm(): IUserDetails {
    return {
      ...new UserDetails(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      subscribeToOffer: this.editForm.get(['subscribeToOffer'])!.value,
      passwordChangedOn: this.editForm.get(['passwordChangedOn'])!.value,
      preferredLanguage: this.editForm.get(['preferredLanguage'])!.value,
      preferredCurrency: this.editForm.get(['preferredCurrency'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserDetails>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
