import { Moment } from 'moment';
import { IEmail } from 'app/shared/model//email.model';
import { IMilestone } from 'app/shared/model//milestone.model';

export const enum DacStatus {
    CREATED = 'CREATED',
    SENT = 'SENT',
    APPROVED = 'APPROVED',
    DECLINED = 'DECLINED',
    INVOICED = 'INVOICED'
}

export interface IDAC {
    id?: number;
    dueDate?: Moment;
    description?: string;
    dacAmount?: number;
    status?: DacStatus;
    email?: IEmail;
    milestones?: IMilestone[];
}

export class DAC implements IDAC {
    constructor(
        public id?: number,
        public dueDate?: Moment,
        public description?: string,
        public dacAmount?: number,
        public status?: DacStatus,
        public email?: IEmail,
        public milestones?: IMilestone[]
    ) {}
}
