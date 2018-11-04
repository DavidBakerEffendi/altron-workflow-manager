import { NgModule } from '@angular/core';

import { WorkflowManagerSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [WorkflowManagerSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [WorkflowManagerSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class WorkflowManagerSharedCommonModule {}
