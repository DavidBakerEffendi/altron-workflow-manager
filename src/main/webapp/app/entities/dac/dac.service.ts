import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDAC } from 'app/shared/model/dac.model';

type EntityResponseType = HttpResponse<IDAC>;
type EntityArrayResponseType = HttpResponse<IDAC[]>;

@Injectable({ providedIn: 'root' })
export class DACService {
    private resourceUrl = SERVER_API_URL + 'api/dacs';

    constructor(private http: HttpClient) {}

    create(dAC: IDAC): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dAC);
        return this.http
            .post<IDAC>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(dAC: IDAC): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dAC);
        return this.http
            .put<IDAC>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDAC>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDAC[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(dAC: IDAC): IDAC {
        const copy: IDAC = Object.assign({}, dAC, {
            dueDate: dAC.dueDate != null && dAC.dueDate.isValid() ? dAC.dueDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dueDate = res.body.dueDate != null ? moment(res.body.dueDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((dAC: IDAC) => {
            dAC.dueDate = dAC.dueDate != null ? moment(dAC.dueDate) : null;
        });
        return res;
    }
}
