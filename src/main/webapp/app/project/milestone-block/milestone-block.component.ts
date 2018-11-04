import { Component, OnInit, Input } from '@angular/core';
import { IMilestone, MilestoneStatus } from 'app/shared/model/milestone.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

@Component({
    selector: 'jhi-milestone-block',
    templateUrl: './milestone-block.component.html'
})
export class MilestoneBlockComponent implements OnInit {
    @Input() milestone: IMilestone;
    @Input() isActive: boolean;
    @Input() prevDate: Moment;
    constructor() {}

    getProgress() {
        const nowTime = moment().unix();
        const startTime = moment(this.prevDate).unix();
        const endTime = moment(this.milestone.dueDate).unix();
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

    debugTime() {
        const nowTime = moment().unix();
        const startTime = moment(this.prevDate).unix();
        const endTime = moment(this.milestone.dueDate).unix();
        return (
            'nowDiff: ' +
            (nowTime - startTime) +
            ', startTime: ' +
            startTime +
            ', endTime: ' +
            endTime +
            ', progress: ' +
            this.getProgress()
        );
    }

    /*getDueDate() {
        return moment(this.milestone.dueDate).format('LLL');
    }*/
    isOverDue() {
        const nowTime = moment().unix();
        const startTime = moment(this.prevDate).unix();
        const endTime = moment(this.milestone.dueDate).unix();
        return nowTime >= endTime || endTime < startTime;
    }

    isComplete() {
        return this.milestone.status === MilestoneStatus.COMPLETE;
    }

    isWIP() {
        return this.milestone.status === MilestoneStatus.WIP;
    }

    getDueDate() {
        const nowTime = moment().unix();
        const endTime = moment(this.milestone.dueDate).unix();
        const startTime = moment(this.prevDate).unix();

        const overDue = nowTime >= endTime;

        if (startTime > endTime) {
            return 'Oops, milestone due date precedes its starting date';
        }
        if (!this.isComplete()) {
            if (overDue) {
                return 'Overdue by ' + Math.ceil((nowTime - endTime) / 86164.09) + ' days'; // 86164.09 is the precise amount of seconds in a day
            } else {
                return Math.floor((endTime - nowTime) / 86164.09) + ' days left';
            }
        } else {
            return 'Milestone Completed';
        }
    }

    ngOnInit() {}
}
