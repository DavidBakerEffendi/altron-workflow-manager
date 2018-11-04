import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDAC } from 'app/shared/model/dac.model';

@Component({
    selector: 'jhi-dac-detail',
    templateUrl: './dac-detail.component.html'
})
export class DACDetailComponent implements OnInit {
    dAC: IDAC;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dAC }) => {
            this.dAC = dAC;
        });
    }

    previousState() {
        window.history.back();
    }
}
