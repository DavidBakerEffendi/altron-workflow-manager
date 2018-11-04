import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowManagerSharedModule } from 'app/shared';
import { COMPANY_ROUTE, CompanyComponent } from './';
import { PipeModule } from '../shared/pipe-module';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { JhiProjectMilestoneComponent } from './project-milestone/project-milestone.component';

@NgModule({
    imports: [
        WorkflowManagerSharedModule,
        RouterModule.forChild([COMPANY_ROUTE]),
        PipeModule.forRoot(),
        MatExpansionModule,
        MatButtonModule,
        MatIconModule
    ],
    declarations: [CompanyComponent, JhiProjectMilestoneComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkflowManagerCompanyModule {}
