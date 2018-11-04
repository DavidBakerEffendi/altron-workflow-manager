import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowManagerSharedModule } from 'app/shared';
import {
    DACComponent,
    DACDetailComponent,
    DACUpdateComponent,
    DACDeletePopupComponent,
    DACDeleteDialogComponent,
    dACRoute,
    dACPopupRoute
} from './';

const ENTITY_STATES = [...dACRoute, ...dACPopupRoute];

@NgModule({
    imports: [WorkflowManagerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [DACComponent, DACDetailComponent, DACUpdateComponent, DACDeleteDialogComponent, DACDeletePopupComponent],
    entryComponents: [DACComponent, DACUpdateComponent, DACDeleteDialogComponent, DACDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkflowManagerDACModule {}
