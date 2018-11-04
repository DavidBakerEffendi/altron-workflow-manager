import { Route } from '@angular/router';

import { MilestoneComponent } from './';

export const MILESTONE_ROUTE: Route = {
    path: 'company/:company/project/:project/milestone/:id',
    component: MilestoneComponent,
    data: {
        authorities: [],
        pageTitle: 'Milestone | Workflow Manager | Altron'
    }
};
