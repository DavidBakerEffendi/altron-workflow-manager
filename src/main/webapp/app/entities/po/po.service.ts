import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPO } from 'app/shared/model/po.model';

type EntityResponseType = HttpResponse<IPO>;
type EntityArrayResponseType = HttpResponse<IPO[]>;

@Injectable({ providedIn: 'root' })
export class POService {
    private resourceUrl = SERVER_API_URL + 'api/pos';

    constructor(private http: HttpClient) {}

    create(pO: IPO): Observable<EntityResponseType> {
        return this.http.post<IPO>(this.resourceUrl, pO, { observe: 'response' });
    }

    update(pO: IPO): Observable<EntityResponseType> {
        return this.http.put<IPO>(this.resourceUrl, pO, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPO>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPO[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
