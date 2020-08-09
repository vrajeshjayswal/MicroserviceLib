import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-details',
        loadChildren: () => import('./user-details/user-details.module').then(m => m.UserAccountUserDetailsModule),
      },
      {
        path: 'user-address',
        loadChildren: () => import('./user-address/user-address.module').then(m => m.UserAccountUserAddressModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class UserAccountEntityModule {}
