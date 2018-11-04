import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowManagerSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { JhiCompanyBlockComponent } from './company-block/company-block.component';
import { MatButtonModule } from '@angular/material/button';
import { PipeModule } from '../shared/pipe-module';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
    imports: [WorkflowManagerSharedModule, RouterModule.forChild([HOME_ROUTE]), PipeModule.forRoot(), MatButtonModule, MatIconModule],
    declarations: [HomeComponent, JhiCompanyBlockComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkflowManagerHomeModule {}
