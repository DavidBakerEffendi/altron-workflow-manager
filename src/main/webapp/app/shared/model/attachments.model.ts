import { IEmail } from 'app/shared/model//email.model';

export interface IAttachments {
    id?: number;
    name?: string;
    attachmentContentType?: string;
    attachment?: any;
    email?: IEmail;
}

export class Attachments implements IAttachments {
    constructor(
        public id?: number,
        public name?: string,
        public attachmentContentType?: string,
        public attachment?: any,
        public email?: IEmail
    ) {}
}
