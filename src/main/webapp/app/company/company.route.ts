import { Route } from '@angular/router';

import { CompanyComponent } from './';

export const COMPANY_ROUTE: Route = {
    path: 'company/:company',
    component: CompanyComponent,
    data: {
        authorities: [],
        pageTitle: 'Company | Workflow Manager | Altron'
    }
};
