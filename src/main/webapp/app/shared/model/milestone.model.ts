import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IPO } from 'app/shared/model//po.model';
import { IDAC } from 'app/shared/model//dac.model';
import { IProject } from 'app/shared/model//project.model';

export const enum MilestoneStatus {
    ACTIVE = 'ACTIVE',
    WIP = 'WIP',
    READY = 'READY',
    COMPLETE = 'COMPLETE'
}

export interface IMilestone {
    id?: number;
    name?: string;
    dueDate?: Moment;
    previousRevenue?: number;
    prereceiptedIncome?: number;
    revenueHoldBack?: number;
    revenueInNextFinYear?: number;
    status?: MilestoneStatus;
    user?: IUser;
    poNumber?: IPO;
    dacs?: IDAC[];
    isprNumber?: IProject;
}

export class Milestone implements IMilestone {
    constructor(
        public id?: number,
        public name?: string,
        public dueDate?: Moment,
        public previousRevenue?: number,
        public prereceiptedIncome?: number,
        public revenueHoldBack?: number,
        public revenueInNextFinYear?: number,
        public status?: MilestoneStatus,
        public user?: IUser,
        public poNumber?: IPO,
        public dacs?: IDAC[],
        public isprNumber?: IProject
    ) {}
}
