import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserAddress } from 'app/shared/model/user-address.model';
import { UserAddressService } from './user-address.service';
import { UserAddressDeleteDialogComponent } from './user-address-delete-dialog.component';

@Component({
  selector: 'jhi-user-address',
  templateUrl: './user-address.component.html',
})
export class UserAddressComponent implements OnInit, OnDestroy {
  userAddresses?: IUserAddress[];
  eventSubscriber?: Subscription;

  constructor(
    protected userAddressService: UserAddressService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.userAddressService.query().subscribe((res: HttpResponse<IUserAddress[]>) => (this.userAddresses = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUserAddresses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUserAddress): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInUserAddresses(): void {
    this.eventSubscriber = this.eventManager.subscribe('userAddressListModification', () => this.loadAll());
  }

  delete(userAddress: IUserAddress): void {
    const modalRef = this.modalService.open(UserAddressDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userAddress = userAddress;
  }
}
