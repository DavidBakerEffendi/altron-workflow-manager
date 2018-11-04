import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowManagerSharedModule } from 'app/shared';
import {
    POComponent,
    PODetailComponent,
    POUpdateComponent,
    PODeletePopupComponent,
    PODeleteDialogComponent,
    pORoute,
    pOPopupRoute
} from './';

const ENTITY_STATES = [...pORoute, ...pOPopupRoute];

@NgModule({
    imports: [WorkflowManagerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [POComponent, PODetailComponent, POUpdateComponent, PODeleteDialogComponent, PODeletePopupComponent],
    entryComponents: [POComponent, POUpdateComponent, PODeleteDialogComponent, PODeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkflowManagerPOModule {}
