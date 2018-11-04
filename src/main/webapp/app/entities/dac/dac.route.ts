import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DAC, DacStatus } from 'app/shared/model/dac.model';
import { DACService } from './dac.service';
import { MilestoneService } from '../milestone/milestone.service';
import { DACComponent } from './dac.component';
import { DACDetailComponent } from './dac-detail.component';
import { DACUpdateComponent } from './dac-update.component';
import { DACDeletePopupComponent } from './dac-delete-dialog.component';
import { IDAC } from 'app/shared/model/dac.model';
import { Milestone, IMilestone } from 'app/shared/model/milestone.model';

@Injectable({ providedIn: 'root' })
export class DACResolve implements Resolve<IDAC> {
    constructor(private dacService: DACService, private milestoneService: MilestoneService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.dacService.find(id).pipe(map((dAC: HttpResponse<DAC>) => dAC.body));
        }
        return of(new DAC());
    }
}

export const dACRoute: Routes = [
    {
        path: 'dac',
        component: DACComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'DACS'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'dac/:id/view',
        component: DACDetailComponent,
        resolve: {
            dAC: DACResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DACS'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'dac/new',
        component: DACUpdateComponent,
        resolve: {
            dAC: DACResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DACS'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'milestone/:mId/dac/new',
        component: DACUpdateComponent,
        resolve: {
            dAC: DACResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DACS'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'dac/:id/edit',
        component: DACUpdateComponent,
        resolve: {
            dAC: DACResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DACS'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dACPopupRoute: Routes = [
    {
        path: 'dac/:id/delete',
        component: DACDeletePopupComponent,
        resolve: {
            dAC: DACResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DACS'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
