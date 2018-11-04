import { Component, Input, OnInit } from '@angular/core';
import { IDAC, DacStatus } from 'app/shared/model/dac.model';
import { IEmail } from 'app/shared/model/email.model';
import { IAttachments } from 'app/shared/model/attachments.model';
import { HttpClient, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

type EntityResponseType = HttpResponse<IEmail>;
type EntityArrayResponseType = HttpResponse<IEmail[]>;

@Component({
    selector: 'jhi-dac-email',
    templateUrl: './dac-email.component.html',
    styleUrls: ['../../styles.scss']
})
export class JhiDACEmailComponent implements OnInit {
    @Input() dac: IDAC;
    @Input() manager: string;

    createdStatus = DacStatus.CREATED;
    sentStatus = DacStatus.SENT;
    approvedStatus = DacStatus.APPROVED;
    declinedStatus = DacStatus.DECLINED;
    invoicedStatus = DacStatus.INVOICED;
    private resourceUrl = SERVER_API_URL + 'api/project-email';
    private temp2: string;
    constructor(private http: HttpClient, private activatedRoute: ActivatedRoute, private dataUtils: JhiDataUtils) {}

    sendEmail(temp: string, statusDAC: DacStatus) {
        this.temp2 = this.dac.email.address;
        this.dac.email.address = temp;
        console.log('Sending to:' + this.dac.email.address);
        this.subscribeToSaveResponse(this.http.post<IEmail>(this.resourceUrl, this.dac.email, { observe: 'response' }));

        this.dac.status = statusDAC;

        this.updateDAC(this.dac).subscribe(
            res => {
                console.log(res);
            },
            err => console.log(err)
        );
        this.dac.email.address = this.temp2;
    }

    ngOnInit() {
        if (this.dac) {
            if (this.dac.email) {
                this.getAttachments();
            }
        }
    }

    getAttachments() {
        this.http.get(SERVER_API_URL + 'api/emails/' + this.dac.email.id + '/attachments').subscribe(
            (res: IAttachments[]) => {
                this.dac.email.attachments = res;
            },
            err => {
                console.log(err);
            }
        );
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    previousState() {
        // window.history.back();
    }

    updateDAC(dac: IDAC): Observable<EntityResponseType> {
        return this.http.put<IDAC>(SERVER_API_URL + 'api/dacs', dac, { observe: 'response' });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEmail>>) {
        result.subscribe((res: HttpResponse<IEmail>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        // this.isSaving = false;
        console.log('Send successful');
        // this.previousState();
    }

    private onSaveError() {
        console.log('Send Failure');
        // this.isSaving = false;
    }
}
