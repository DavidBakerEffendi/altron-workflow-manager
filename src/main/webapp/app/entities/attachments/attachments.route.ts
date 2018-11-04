import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Attachments } from 'app/shared/model/attachments.model';
import { AttachmentsService } from './attachments.service';
import { AttachmentsComponent } from './attachments.component';
import { AttachmentsDetailComponent } from './attachments-detail.component';
import { AttachmentsUpdateComponent } from './attachments-update.component';
import { AttachmentsDeletePopupComponent } from './attachments-delete-dialog.component';
import { IAttachments } from 'app/shared/model/attachments.model';

@Injectable({ providedIn: 'root' })
export class AttachmentsResolve implements Resolve<IAttachments> {
    constructor(private service: AttachmentsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((attachments: HttpResponse<Attachments>) => attachments.body));
        }
        return of(new Attachments());
    }
}

export const attachmentsRoute: Routes = [
    {
        path: 'attachments',
        component: AttachmentsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attachments'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'attachments/:id/view',
        component: AttachmentsDetailComponent,
        resolve: {
            attachments: AttachmentsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attachments'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'attachments/new',
        component: AttachmentsUpdateComponent,
        resolve: {
            attachments: AttachmentsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attachments'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'email/:eId/attachments/new',
        component: AttachmentsUpdateComponent,
        resolve: {
            attachments: AttachmentsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attachments'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'attachments/:id/edit',
        component: AttachmentsUpdateComponent,
        resolve: {
            attachments: AttachmentsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attachments'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attachmentsPopupRoute: Routes = [
    {
        path: 'attachments/:id/delete',
        component: AttachmentsDeletePopupComponent,
        resolve: {
            attachments: AttachmentsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Attachments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
