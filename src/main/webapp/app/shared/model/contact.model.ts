import { IProject } from 'app/shared/model//project.model';

export interface IContact {
    id?: number;
    firstName?: string;
    lastName?: string;
    workNumber?: string;
    cellNumber?: string;
    email?: string;
    isprNumbers?: IProject[];
}

export class Contact implements IContact {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public workNumber?: string,
        public cellNumber?: string,
        public email?: string,
        public isprNumbers?: IProject[]
    ) {}
}
