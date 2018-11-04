import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Milestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from './milestone.service';
import { MilestoneComponent } from './milestone.component';
import { MilestoneDetailComponent } from './milestone-detail.component';
import { MilestoneUpdateComponent } from './milestone-update.component';
import { MilestoneDeletePopupComponent } from './milestone-delete-dialog.component';
import { IMilestone } from 'app/shared/model/milestone.model';

@Injectable({ providedIn: 'root' })
export class MilestoneResolve implements Resolve<IMilestone> {
    constructor(private service: MilestoneService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((milestone: HttpResponse<Milestone>) => milestone.body));
        }
        return of(new Milestone());
    }
}

export const milestoneRoute: Routes = [
    {
        path: 'milestone',
        component: MilestoneComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Milestones'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'milestone/:id/view',
        component: MilestoneDetailComponent,
        resolve: {
            milestone: MilestoneResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Milestones'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'milestone/new',
        component: MilestoneUpdateComponent,
        resolve: {
            milestone: MilestoneResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Milestones'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'milestone/:id/edit',
        component: MilestoneUpdateComponent,
        resolve: {
            milestone: MilestoneResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Milestones'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const milestonePopupRoute: Routes = [
    {
        path: 'milestone/:id/delete',
        component: MilestoneDeletePopupComponent,
        resolve: {
            milestone: MilestoneResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Milestones'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
