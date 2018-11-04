import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowManagerSharedModule } from 'app/shared';
import { MILESTONE_ROUTE, MilestoneComponent } from './';
import { JhiDACEmailComponent } from './dac-email/dac-email.component';
import { PipeModule } from '../shared/pipe-module';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule, MatNativeDateModule } from '@angular/material';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatChipsModule } from '@angular/material/chips';
import 'hammerjs';

@NgModule({
    imports: [
        WorkflowManagerSharedModule,
        RouterModule.forChild([MILESTONE_ROUTE]),
        PipeModule.forRoot() /* Other to follow */,
        BrowserAnimationsModule,
        MatIconModule,
        MatInputModule,
        MatFormFieldModule,
        MatSelectModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatButtonModule,
        MatExpansionModule,
        MatChipsModule
    ],
    declarations: [MilestoneComponent, JhiDACEmailComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkflowManagerMilestoneModule {}
