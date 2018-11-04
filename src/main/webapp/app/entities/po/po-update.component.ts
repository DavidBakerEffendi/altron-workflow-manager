import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPO } from 'app/shared/model/po.model';
import { POService } from './po.service';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone';
import { SERVER_API_URL } from 'app/app.constants';

@Component({
    selector: 'jhi-po-update',
    templateUrl: './po-update.component.html',
    styleUrls: ['../../styles.scss']
})
export class POUpdateComponent implements OnInit {
    pO: IPO;
    isSaving: boolean;
    mId: number;
    milestone: IMilestone;

    constructor(
        private pOService: POService,
        private milestoneService: MilestoneService,
        private http: HttpClient,
        private activatedRoute: ActivatedRoute) { }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pO }) => {
            this.pO = pO;
        });
        this.activatedRoute.params.subscribe(params => {
            this.mId = params['mId'];
            if (this.mId) {
                this.milestoneService.find(this.mId).subscribe(
                    (res: HttpResponse<IMilestone>) => {
                        this.milestone = res.body;
                    },
                    (res: HttpErrorResponse) => console.log(res.message)
                );
            }
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.pO.id !== undefined) {
            this.subscribeToSaveResponse(this.pOService.update(this.pO));
        } else {
            this.subscribeToSaveResponse(this.pOService.create(this.pO));
        }
    }

    /**
     * Places the relationship between milestone and DAC.
     * @param milestoneId Milestone ID
     * @param dacId DAC ID
     */
    private linkPoAndMilestone(milestoneId: number, poId: number) {
        this.http.get(SERVER_API_URL + 'api/relation-resource/link-po-' + poId + '-to-milestone-' + milestoneId)
            .subscribe(res => {
                console.log('Link success!');
                console.log(res);
            }, err => {
                console.log('Link failure!');
                console.log(err);
            });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPO>>) {
        result.subscribe((res: HttpResponse<IPO>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(newPo: IPO) {
        this.isSaving = false;
        if (this.mId) {
            this.linkPoAndMilestone(this.mId, newPo.id);
        }
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
