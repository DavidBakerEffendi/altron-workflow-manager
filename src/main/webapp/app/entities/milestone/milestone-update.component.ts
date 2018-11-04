import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from './milestone.service';
import { IUser, UserService } from 'app/core';
import { IPO } from 'app/shared/model/po.model';
import { POService } from 'app/entities/po';
import { IDAC } from 'app/shared/model/dac.model';
import { DACService } from 'app/entities/dac';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
    selector: 'jhi-milestone-update',
    templateUrl: './milestone-update.component.html',
    styleUrls: ['../../styles.scss']
})
export class MilestoneUpdateComponent implements OnInit {
    milestone: IMilestone;
    isSaving: boolean;

    users: IUser[];

    pos: IPO[];

    dacs: IDAC[];

    projects: IProject[];
    dueDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private milestoneService: MilestoneService,
        private userService: UserService,
        private pOService: POService,
        private dACService: DACService,
        private projectService: ProjectService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ milestone }) => {
            this.milestone = milestone;
            this.dueDate = this.milestone.dueDate != null ? this.milestone.dueDate.format(DATE_TIME_FORMAT) : null;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.pOService.query().subscribe(
            (res: HttpResponse<IPO[]>) => {
                this.pos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.dACService.query().subscribe(
            (res: HttpResponse<IDAC[]>) => {
                this.dacs = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.projectService.query().subscribe(
            (res: HttpResponse<IProject[]>) => {
                this.projects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.milestone.dueDate = this.dueDate != null ? moment(this.dueDate, DATE_TIME_FORMAT) : null;
        if (this.milestone.id !== undefined) {
            this.subscribeToSaveResponse(this.milestoneService.update(this.milestone));
        } else {
            this.subscribeToSaveResponse(this.milestoneService.create(this.milestone));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMilestone>>) {
        result.subscribe((res: HttpResponse<IMilestone>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackPOById(index: number, item: IPO) {
        return item.id;
    }

    trackDACById(index: number, item: IDAC) {
        return item.id;
    }

    trackProjectById(index: number, item: IProject) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
