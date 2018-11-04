import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IAttachments } from 'app/shared/model/attachments.model';
import { AttachmentsService } from './attachments.service';
import { IEmail } from 'app/shared/model/email.model';
import { EmailService } from 'app/entities/email';
import { SERVER_API_URL } from 'app/app.constants';

@Component({
    selector: 'jhi-attachments-update',
    templateUrl: './attachments-update.component.html',
    styleUrls: ['../../styles.scss']
})
export class AttachmentsUpdateComponent implements OnInit {
    attachments: IAttachments;
    isSaving: boolean;
    eId: number;
    email: IEmail;

    emails: IEmail[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private attachmentsService: AttachmentsService,
        private emailService: EmailService,
        private activatedRoute: ActivatedRoute,
        private http: HttpClient
    ) { }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ attachments }) => {
            this.attachments = attachments;
        });
        this.emailService.query().subscribe(
            (res: HttpResponse<IEmail[]>) => {
                this.emails = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.activatedRoute.params.subscribe(params => {
            this.eId = params['eId'];
            if (this.eId) {
                this.emailService.find(this.eId).subscribe(
                    (res: HttpResponse<IEmail>) => {
                        this.email = res.body;
                    },
                    (res: HttpErrorResponse) => console.log(res)
                );
            }
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.attachments.id !== undefined) {
            this.subscribeToSaveResponse(this.attachmentsService.update(this.attachments));
        } else {
            this.subscribeToSaveResponse(this.attachmentsService.create(this.attachments));
        }
    }

    /**
     * Links attachment to an email.
     *
     * @param attachmentId Attachment ID.
     * @param emailId Email ID
     */
    private linkAttachmentAndEmail(attachmentId: number, emailId: number) {
        this.http.get(SERVER_API_URL + 'api/relation-resource/link-attachment-' + attachmentId + '-to-email-' + emailId)
            .subscribe(res => {
                console.log('Link success!');
                console.log(res);
            }, err => {
                console.log('Link failure!');
                console.log(err);
            });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAttachments>>) {
        result.subscribe((res: HttpResponse<IAttachments>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(newAttachment: IAttachments) {
        this.isSaving = false;
        if (this.eId) {
            this.linkAttachmentAndEmail(this.eId, newAttachment.id);
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
}
