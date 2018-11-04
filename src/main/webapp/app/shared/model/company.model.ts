import { IProject } from 'app/shared/model//project.model';

export interface ICompany {
    id?: number;
    companyName?: string;
    description?: string;
    headName?: string;
    headEmail?: string;
    headNumber?: string;
    projects?: IProject[];
}

export class Company implements ICompany {
    constructor(
        public id?: number,
        public companyName?: string,
        public description?: string,
        public headName?: string,
        public headEmail?: string,
        public headNumber?: string,
        public projects?: IProject[]
    ) {}
}
