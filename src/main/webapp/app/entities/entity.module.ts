import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WorkflowManagerContactModule } from './contact/contact.module';
import { WorkflowManagerCompanyModule } from './company/company.module';
import { WorkflowManagerProjectModule } from './project/project.module';
import { WorkflowManagerMilestoneModule } from './milestone/milestone.module';
import { WorkflowManagerDACModule } from './dac/dac.module';
import { WorkflowManagerPOModule } from './po/po.module';
import { WorkflowManagerEmailModule } from './email/email.module';
import { WorkflowManagerAttachmentsModule } from './attachments/attachments.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        WorkflowManagerContactModule,
        WorkflowManagerCompanyModule,
        WorkflowManagerProjectModule,
        WorkflowManagerMilestoneModule,
        WorkflowManagerDACModule,
        WorkflowManagerPOModule,
        WorkflowManagerEmailModule,
        WorkflowManagerAttachmentsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkflowManagerEntityModule {}
