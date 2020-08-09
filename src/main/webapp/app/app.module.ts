import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { UserAccountSharedModule } from 'app/shared/shared.module';
import { UserAccountCoreModule } from 'app/core/core.module';
import { UserAccountAppRoutingModule } from './app-routing.module';
import { UserAccountHomeModule } from './home/home.module';
import { UserAccountEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    UserAccountSharedModule,
    UserAccountCoreModule,
    UserAccountHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    UserAccountEntityModule,
    UserAccountAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class UserAccountAppModule {}
