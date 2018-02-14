import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Amont } from './amont.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Amont>;

@Injectable()
export class AmontService {

    private resourceUrl =  SERVER_API_URL + 'api/amonts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/amonts';

    constructor(private http: HttpClient) { }

    create(amont: Amont): Observable<EntityResponseType> {
        const copy = this.convert(amont);
        return this.http.post<Amont>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(amont: Amont): Observable<EntityResponseType> {
        const copy = this.convert(amont);
        return this.http.put<Amont>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Amont>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Amont[]>> {
        const options = createRequestOption(req);
        return this.http.get<Amont[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Amont[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Amont[]>> {
        const options = createRequestOption(req);
        return this.http.get<Amont[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Amont[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Amont = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Amont[]>): HttpResponse<Amont[]> {
        const jsonResponse: Amont[] = res.body;
        const body: Amont[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Amont.
     */
    private convertItemFromServer(amont: Amont): Amont {
        const copy: Amont = Object.assign({}, amont);
        return copy;
    }

    /**
     * Convert a Amont to a JSON which can be sent to the server.
     */
    private convert(amont: Amont): Amont {
        const copy: Amont = Object.assign({}, amont);
        return copy;
    }
}
