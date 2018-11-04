import { Moment } from 'moment';
import { IContact } from 'app/shared/model//contact.model';
import { IMilestone } from 'app/shared/model//milestone.model';
import { ICompany } from 'app/shared/model//company.model';

export interface IProject {
    id?: number;
    isprNumber?: string;
    isprAmount?: number;
    isprDescription?: string;
    startDate?: Moment;
    contact?: IContact;
    milestones?: IMilestone[];
    cc?: ICompany;
}

export class Project implements IProject {
    constructor(
        public id?: number,
        public isprNumber?: string,
        public isprAmount?: number,
        public isprDescription?: string,
        public startDate?: Moment,
        public contact?: IContact,
        public milestones?: IMilestone[],
        public cc?: ICompany
    ) {}
}
