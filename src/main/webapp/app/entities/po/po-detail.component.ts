import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPO } from 'app/shared/model/po.model';

@Component({
    selector: 'jhi-po-detail',
    templateUrl: './po-detail.component.html'
})
export class PODetailComponent implements OnInit {
    pO: IPO;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pO }) => {
            this.pO = pO;
        });
    }

    previousState() {
        window.history.back();
    }
}
