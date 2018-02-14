import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Measurement } from './measurement.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Measurement>;

@Injectable()
export class MeasurementService {

    private resourceUrl =  SERVER_API_URL + 'api/measurements';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/measurements';

    constructor(private http: HttpClient) { }

    create(measurement: Measurement): Observable<EntityResponseType> {
        const copy = this.convert(measurement);
        return this.http.post<Measurement>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(measurement: Measurement): Observable<EntityResponseType> {
        const copy = this.convert(measurement);
        return this.http.put<Measurement>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Measurement>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Measurement[]>> {
        const options = createRequestOption(req);
        return this.http.get<Measurement[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Measurement[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Measurement[]>> {
        const options = createRequestOption(req);
        return this.http.get<Measurement[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Measurement[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Measurement = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Measurement[]>): HttpResponse<Measurement[]> {
        const jsonResponse: Measurement[] = res.body;
        const body: Measurement[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Measurement.
     */
    private convertItemFromServer(measurement: Measurement): Measurement {
        const copy: Measurement = Object.assign({}, measurement);
        return copy;
    }

    /**
     * Convert a Measurement to a JSON which can be sent to the server.
     */
    private convert(measurement: Measurement): Measurement {
        const copy: Measurement = Object.assign({}, measurement);
        return copy;
    }
}
