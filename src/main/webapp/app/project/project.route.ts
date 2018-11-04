import { Route } from '@angular/router';

import { ProjectComponent } from './';

export const PROJECT_ROUTE: Route = {
    path: 'company/:company/project/:project',
    component: ProjectComponent,
    data: {
        authorities: [],
        pageTitle: 'Project | Workflow Manager | Altron'
    }
};
