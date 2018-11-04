import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IAttachments } from 'app/shared/model/attachments.model';

@Component({
    selector: 'jhi-attachments-detail',
    templateUrl: './attachments-detail.component.html'
})
export class AttachmentsDetailComponent implements OnInit {
    attachments: IAttachments;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ attachments }) => {
            this.attachments = attachments;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
