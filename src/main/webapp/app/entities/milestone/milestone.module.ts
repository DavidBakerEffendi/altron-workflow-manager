import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowManagerSharedModule } from 'app/shared';
import { WorkflowManagerAdminModule } from 'app/admin/admin.module';
import {
    MilestoneComponent,
    MilestoneDetailComponent,
    MilestoneUpdateComponent,
    MilestoneDeletePopupComponent,
    MilestoneDeleteDialogComponent,
    milestoneRoute,
    milestonePopupRoute
} from './';

const ENTITY_STATES = [...milestoneRoute, ...milestonePopupRoute];

@NgModule({
    imports: [WorkflowManagerSharedModule, WorkflowManagerAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MilestoneComponent,
        MilestoneDetailComponent,
        MilestoneUpdateComponent,
        MilestoneDeleteDialogComponent,
        MilestoneDeletePopupComponent
    ],
    entryComponents: [MilestoneComponent, MilestoneUpdateComponent, MilestoneDeleteDialogComponent, MilestoneDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkflowManagerMilestoneModule {}
