import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiTitleCasePipe } from '../shared/string-pipes';
import { Title } from '@angular/platform-browser';
import { IProject } from 'app/shared/model/project.model';
import { IContact } from 'app/shared/model/contact.model';
import { ICompany } from 'app/shared/model/company.model';
import { IMilestone, MilestoneStatus } from 'app/shared/model/milestone.model';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { DacStatus, IDAC } from 'app/shared/model/dac.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { Principal } from 'app/core';

@Component({
    selector: 'jhi-project',
    templateUrl: './project.component.html',
    providers: [JhiTitleCasePipe],
    styleUrls: ['../styles.scss']
})
export class ProjectComponent implements OnInit {
    private companyName: String;
    private projectName: String;
    private project: IProject;
    private editAble: boolean;
    private activeMilestone: number;
    private milestoneStatistics: Object[];
    private invoicedIncome: number;
    private totalDacs: number;
    private totalPo: number;
    private contactName: String;
    private contactEmail: String;

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
            this.editAble = false;
            this.invoicedIncome = 0.0;
            this.totalDacs = 0;
            this.totalPo = 0;
            this.http.get(SERVER_API_URL + 'api/project/get-' + params['project'].toLowerCase()).subscribe(
                res => {
                    if (res === null) {
                        this.previousState();
                    } else {
                        this.project = res as IProject;
                        this.titleService.setTitle(this.titleCasePipe.transform(this.project.isprNumber) + ' | Altron Workflow Manager');
                        this.getInvoicedIncome();
                        this.getTotalDacs();
                        this.getTotalPOs();
                        this.displayStatistics();
                    }
                },
                err => {
                    console.log(err);
                    this.previousState();
                }
            );
        });
    }

    ngOnInit() {}

    /**
     * Checks if the entire project is completed, by checking all milestones are completed.
     */
    isCompleted() {
        if (!this.project) {
            return true;
        }

        if (!this.project.milestones || this.project.milestones === null) {
            return true;
        }
        if (this.project.milestones.length === 0) {
            return true;
        }
        for (const milestone of this.project.milestones) {
            if (milestone.status !== MilestoneStatus.COMPLETE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the project to the database.
     */
    updateProject() {
        if (this.project !== undefined) {
            this.http.put(SERVER_API_URL + 'api/projects', this.project).subscribe();
        }
    }

    /**
     * Returns the current moment in time as a Moment.
     *
     * @return A Moment type representing the present.
     */
    getNow() {
        return moment();
    }

    /**
     * Returns representative string of the project's start
     * date in the form: 'Month DD, YYYY', for example 'October 11, 2018'.
     *
     * @return The formatted string of the date.
     */
    displayDate() {
        if (this.project) {
            return moment(this.project.startDate).format('LL');
        } else {
            return moment().format('LL');
        }
    }

    displayDatePrecise(date: Moment) {
        return moment(date).format('LLL');
    }

    /**
     * Computes a milestone's starting date, by using either the previous
     * milestone's due date or the project's starting date.
     *
     * @param i The index of the milestone in project.milestones.
     *
     * @return The expected starting date for a milestone.
     */
    getPrevDate(i: number) {
        if (i === 0) {
            return this.project.startDate;
        } else {
            return this.project.milestones[i - 1].dueDate;
        }
    }
    /**
     * Obtains database statistics from the server.
     */
    displayStatistics() {
        this.http.get(SERVER_API_URL + 'api/statistics/milestone-stats/' + this.project.milestones[0].id).subscribe(
            (res: Object[]) => {
                this.milestoneStatistics = res;
            },
            err => {
                console.log(err);
            }
        );
    }
    milestoneDueDateStatus(i: number) {
        const nowTime = moment().unix();
        const startTime = moment(this.getPrevDate(i)).unix();
        const endTime = moment(this.project.milestones[i].dueDate).unix();

        const overDue = nowTime >= endTime;

        if (startTime > endTime) {
            return 'this milestone\'s due date precedes its starting date.';
        }
        if (!this.milestoneIsComplete(i)) {
            if (overDue) {
                return 'this milestone is overdue by ' + Math.ceil((nowTime - endTime) / 86164.09) + ' days.'; // 86164.09 is the precise amount of seconds in a day
            } else {
                const daysLeft = Math.floor((endTime - nowTime) / 86164.09);
                if (daysLeft === 0) {
                    return 'less than 24 hours until deadline.';
                } else if (daysLeft === 1) {
                    return 'about 1 day left.';
                }
                return 'about ' + daysLeft + ' days left.';
            }
        } else {
            return 'milestone completed.';
        }
    }

    /**
     * Computes the milestone's progress to its due date.
     * Completion of the milestone is ignored.
     *
     * @param i The index of the milestone in project.milestones.
     *
     * @return an integer in the range [0, 100] depicting the milestone's progress.
     */
    milestoneGetProgress(i: number) {
        const nowTime = moment().unix();
        const startTime = moment(this.getPrevDate(i)).unix();
        const endTime = moment(this.project.milestones[i].dueDate).unix();
        if (startTime === endTime) {
            return 0;
        } else if (startTime > endTime) {
            return 100;
        }
        let progress = 1.0 * (nowTime - startTime) / (endTime - startTime);
        if (progress === NaN) {
            progress = 1.0;
        } else if (progress > 1.0) {
            progress = 1.0;
        } else if (progress < 0.0) {
            progress = 0.0;
        }
        return Math.floor(progress * 100);
    }

    /**
     * Checks if a milestone is overdue, or if it's due date precedes its
     * start date. Completion of the milestone is ignored.
     *
     * @param i The index of the milestone in project.milestones.
     *
     * @return TRUE if the milestone's due date has passed or if it's due
     * date precedes its start date, FALSE otherwise.
     */
    milestoneIsOverDue(i: number) {
        const nowTime = moment().unix();
        const startTime = moment(this.getPrevDate(i)).unix();
        const endTime = moment(this.project.milestones[i].dueDate).unix();
        return nowTime >= endTime || endTime < startTime;
    }

    /**
     * Checks if a milestone is completed.
     *
     * @param i The index of the milestone in project.milestones.
     *
     * @return TRUE if the milestone's status is 'COMPLETE', FALSE otherwise.
     */
    milestoneIsComplete(i: number) {
        return this.project.milestones[i].status === MilestoneStatus.COMPLETE;
    }

    /**
     * ASYNCRONOUS, use only after project was found.
     * Queries the current project's total invoiced income via its DACs.
     */
    getInvoicedIncome() {
        this.http.get(SERVER_API_URL + 'api/project/get-proj-' + this.project.id + '-invoiced-income').subscribe(
            (res: number) => {
                this.invoicedIncome = res;
            },
            err => {
                console.log(err);
            }
        );
    }

    getExpectedIncome() {
        this.http.get(SERVER_API_URL + 'api/project/get-project-' + this.project.id + '-projected-income').subscribe(
            (res: number) => {
                this.project.isprAmount = res;
            },
            err => {
                console.log(err);
            }
        );
    }

    getTotalDacs() {
        this.http.get(SERVER_API_URL + 'api/project/get-project-' + this.project.id + '-dacs').subscribe(
            (res: number) => {
                this.totalDacs = res;
            },
            err => {
                console.log(err);
            }
        );
    }

    getTotalPOs() {
        this.http.get(SERVER_API_URL + 'api/project/get-project-' + this.project.id + '-pos').subscribe(
            (res: number) => {
                this.totalPo = res;
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
