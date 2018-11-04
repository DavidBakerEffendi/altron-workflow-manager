import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowManagerSharedModule } from 'app/shared';
import { PROJECT_ROUTE, ProjectComponent } from './';
import { PipeModule } from '../shared/pipe-module';
import { MilestoneBlockComponent } from './milestone-block/milestone-block.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule, MatNativeDateModule } from '@angular/material';
import { MatExpansionModule } from '@angular/material/expansion';

import { MatButtonModule } from '@angular/material/button';

import 'hammerjs';

@NgModule({
    imports: [
        WorkflowManagerSharedModule,
        RouterModule.forChild([PROJECT_ROUTE]),
        PipeModule.forRoot(),
        BrowserAnimationsModule,
        MatIconModule,
        MatInputModule,
        MatFormFieldModule,
        MatSelectModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatButtonModule,
        MatExpansionModule
    ],
    declarations: [ProjectComponent, MilestoneBlockComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkflowManagerProjectModule {}
