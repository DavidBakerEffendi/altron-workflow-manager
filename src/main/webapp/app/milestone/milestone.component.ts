import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { JhiTitleCasePipe } from '../shared/string-pipes';

import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { IDAC } from 'app/shared/model/dac.model';
import { Principal } from 'app/core';
import { IMilestone } from 'app/shared/model/milestone.model';

@Component({
    selector: 'jhi-milestone',
    templateUrl: './milestone.component.html',
    styleUrls: ['../styles.scss'],
    providers: [JhiTitleCasePipe]
})
export class MilestoneComponent implements OnInit {
    private companyName: String;
    private projectName: String;
    private milestone: IMilestone;
    private statistics: Object[];
    private daysLeft: number;

    isDisabled: boolean;

    statuses: Status[] = [{ value: 'Sent' }, { value: 'Received' }, { value: 'Processing' }, { value: 'Approved' }, { value: 'Declined' }];

    constructor(
        private route: ActivatedRoute,
        private titleService: Title,
        private titleCasePipe: JhiTitleCasePipe,
        private http: HttpClient,
        private principal: Principal,
        private router: Router
    ) {
        this.route.params.subscribe(params => {
            if (!this.isAuthenticated()) {
                router.navigate(['#']);
            }
            this.companyName = params['company'];
            this.projectName = params['project'];
            this.http.get(SERVER_API_URL + 'api/milestones/' + params['id']).subscribe(
                (res: IMilestone) => {
                    if (res === null) {
                        this.previousState();
                    } else {
                        this.milestone = res;
                        this.displayStatistics();
                        this.getDACs();
                        this.titleService.setTitle(
                            'Milestone ' +
                                this.milestone.id +
                                ' | ' +
                                this.titleCasePipe.transform(this.projectName) +
                                ' | ' +
                                this.titleCasePipe.transform(this.companyName)
                        );
                    }
                },
                err => {
                    console.log(err);
                    this.previousState();
                }
            );
        });
    }

    disabletextArea() {
        this.isDisabled = !this.isDisabled;
    }

    ngOnInit() {
        this.isDisabled = true;
    }

    /**
     * Obtains database statistics from the server.
     */
    displayStatistics() {
        this.http.get(SERVER_API_URL + 'api/statistics/milestone-stats/' + this.milestone.id).subscribe(
            (res: Object[]) => {
                this.statistics = res;
                const diff = Math.abs(new Date().getTime() - new Date(this.statistics['dueDate']).getTime());
                this.daysLeft = Math.ceil(diff / (1000 * 3600 * 24));
            },
            err => {
                console.log(err);
            }
        );
    }

    /**
     * Gets all the DACs attached to this milestone.
     */
    getDACs() {
        this.http.get(SERVER_API_URL + 'api/dacs/milestone-' + this.milestone.id).subscribe(
            (res: IDAC[]) => {
                this.milestone.dacs = res;
            },
            err => {
                console.log(err);
            }
        );
    }

    /**
     * Checks if the user is authenticated.
     */
    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    previousState() {
        window.history.back();
    }
}

export interface Status {
    value: string;
}
