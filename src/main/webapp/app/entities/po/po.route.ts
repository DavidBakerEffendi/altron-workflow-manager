import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PO } from 'app/shared/model/po.model';
import { POService } from './po.service';
import { POComponent } from './po.component';
import { PODetailComponent } from './po-detail.component';
import { POUpdateComponent } from './po-update.component';
import { PODeletePopupComponent } from './po-delete-dialog.component';
import { IPO } from 'app/shared/model/po.model';

@Injectable({ providedIn: 'root' })
export class POResolve implements Resolve<IPO> {
    constructor(private service: POService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((pO: HttpResponse<PO>) => pO.body));
        }
        return of(new PO());
    }
}

export const pORoute: Routes = [
    {
        path: 'po',
        component: POComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'POS'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'po/:id/view',
        component: PODetailComponent,
        resolve: {
            pO: POResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'POS'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'po/new',
        component: POUpdateComponent,
        resolve: {
            pO: POResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'POS'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'milestone/:mId/po/new',
        component: POUpdateComponent,
        resolve: {
            pO: POResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'POS'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'po/:id/edit',
        component: POUpdateComponent,
        resolve: {
            pO: POResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'POS'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pOPopupRoute: Routes = [
    {
        path: 'po/:id/delete',
        component: PODeletePopupComponent,
        resolve: {
            pO: POResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'POS'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
