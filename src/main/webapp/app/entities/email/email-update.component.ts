import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IEmail } from 'app/shared/model/email.model';
import { EmailService } from './email.service';
import { DACService } from '../dac/dac.service';
import { IDAC } from 'app/shared/model/dac.model';
import { SERVER_API_URL } from 'app/app.constants';

@Component({
    selector: 'jhi-email-update',
    templateUrl: './email-update.component.html',
    styleUrls: ['../../styles.scss']
})
export class EmailUpdateComponent implements OnInit {
    email: IEmail;
    isSaving: boolean;
    dId: number;

    dac: IDAC;

    constructor(
        private emailService: EmailService,
        private activatedRoute: ActivatedRoute,
        private dacService: DACService,
        private http: HttpClient) { }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ email }) => {
            this.email = email;
        });
        this.activatedRoute.params.subscribe(params => {
            this.dId = params['dId'];
            if (this.dId) {
                this.dacService.find(this.dId).subscribe(
                    (res: HttpResponse<IDAC>) => {
                        this.dac = res.body;
                    },
                    (res: HttpErrorResponse) => console.log(res)
                );
            }
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.email.id !== undefined) {
            this.subscribeToSaveResponse(this.emailService.update(this.email));
        } else {
            this.subscribeToSaveResponse(this.emailService.create(this.email));
        }
    }

    /**
     * Creates a relationship between DAC and Email
     * @param milestoneId Milestone ID
     * @param dacId DAC ID
     */
    private linkDacAndEmail(emailId: number, dacId: number) {
        this.http.get(SERVER_API_URL + 'api/relation-resource/link-email-' + emailId + '-to-dac-' + dacId)
            .subscribe(res => {
                console.log('Link success!');
                console.log(res);
            }, err => {
                console.log('Link failure!');
                console.log(err);
            });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEmail>>) {
        result.subscribe((res: HttpResponse<IEmail>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(savedEmail: IEmail) {
        this.isSaving = false;
        if (this.dac) {
            this.linkDacAndEmail(savedEmail.id, this.dac.id);
        }
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
