import { IMilestone } from 'app/shared/model//milestone.model';

export interface IPO {
    id?: number;
    poAmount?: number;
    milestones?: IMilestone[];
}

export class PO implements IPO {
    constructor(public id?: number, public poAmount?: number, public milestones?: IMilestone[]) {}
}
