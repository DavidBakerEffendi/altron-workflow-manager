import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowManagerSharedModule } from 'app/shared';
import {
    AttachmentsComponent,
    AttachmentsDetailComponent,
    AttachmentsUpdateComponent,
    AttachmentsDeletePopupComponent,
    AttachmentsDeleteDialogComponent,
    attachmentsRoute,
    attachmentsPopupRoute
} from './';

const ENTITY_STATES = [...attachmentsRoute, ...attachmentsPopupRoute];

@NgModule({
    imports: [WorkflowManagerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AttachmentsComponent,
        AttachmentsDetailComponent,
        AttachmentsUpdateComponent,
        AttachmentsDeleteDialogComponent,
        AttachmentsDeletePopupComponent
    ],
    entryComponents: [AttachmentsComponent, AttachmentsUpdateComponent, AttachmentsDeleteDialogComponent, AttachmentsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkflowManagerAttachmentsModule {}
