import { IAttachments } from 'app/shared/model//attachments.model';

export interface IEmail {
    id?: number;
    address?: string;
    subject?: string;
    body?: string;
    attachments?: IAttachments[];
}

export class Email implements IEmail {
    constructor(
        public id?: number,
        public address?: string,
        public subject?: string,
        public body?: string,
        public attachments?: IAttachments[]
    ) {}
}
