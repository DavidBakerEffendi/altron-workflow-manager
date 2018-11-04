import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IDAC } from 'app/shared/model/dac.model';
import { DACService } from './dac.service';
import { IEmail } from 'app/shared/model/email.model';
import { EmailService } from 'app/entities/email';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';
import { SERVER_API_URL } from 'app/app.constants';

@Component({
    selector: 'jhi-dac-update',
    templateUrl: './dac-update.component.html',
    styleUrls: ['../../styles.scss']
})
export class DACUpdateComponent implements OnInit {
    dAC: IDAC;
    isSaving: boolean;

    emails: IEmail[];

    milestone: IMilestone;
    mId: number;
    dueDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private dACService: DACService,
        private emailService: EmailService,
        private milestoneService: MilestoneService,
        private activatedRoute: ActivatedRoute,
        private http: HttpClient
    ) { }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ dAC }) => {
            this.dAC = dAC;
            this.dueDate = this.dAC.dueDate != null ? this.dAC.dueDate.format(DATE_TIME_FORMAT) : null;
        });
        this.activatedRoute.params.subscribe(params => {
            this.mId = params['mId'];
            if (this.mId) {
                this.milestoneService.find(this.mId).subscribe(
                    (res: HttpResponse<IMilestone>) => {
                        this.milestone = res.body;
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            }
        });
        this.emailService.query({ filter: 'dac-is-null' }).subscribe(
            (res: HttpResponse<IEmail[]>) => {
                if (!this.dAC.email || !this.dAC.email.id) {
                    this.emails = res.body;
                } else {
                    this.emailService.find(this.dAC.email.id).subscribe(
                        (subRes: HttpResponse<IEmail>) => {
                            this.emails = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.dAC.dueDate = this.dueDate != null ? moment(this.dueDate, DATE_TIME_FORMAT) : null;
        if (this.dAC.id !== undefined) {
            this.subscribeToSaveResponse(this.dACService.update(this.dAC));
        } else {
            this.subscribeToSaveResponse(this.dACService.create(this.dAC));
        }
    }

    /**
     * Places the relationship between milestone and DAC.
     * @param milestoneId Milestone ID
     * @param dacId DAC ID
     */
    private linkDacAndMilestone(milestoneId: number, dacId: number) {
        this.http.get(SERVER_API_URL + 'api/relation-resource/link-milestone-' + milestoneId + '-to-dac-' + dacId)
            .subscribe(res => {
                console.log('Link success!');
                console.log(res);
            }, err => {
                console.log('Link failure!');
                console.log(err);
            });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDAC>>) {
        result.subscribe((res: HttpResponse<IDAC>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(savedDac: IDAC) {
        this.isSaving = false;
        if (this.milestone) {
            this.linkDacAndMilestone(this.milestone.id, savedDac.id);
        }
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackEmailById(index: number, item: IEmail) {
        return item.id;
    }

    trackMilestoneById(index: number, item: IMilestone) {
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
