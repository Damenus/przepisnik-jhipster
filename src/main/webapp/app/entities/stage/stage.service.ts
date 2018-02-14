import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Stage } from './stage.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Stage>;

@Injectable()
export class StageService {

    private resourceUrl =  SERVER_API_URL + 'api/stages';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/stages';

    constructor(private http: HttpClient) { }

    create(stage: Stage): Observable<EntityResponseType> {
        const copy = this.convert(stage);
        return this.http.post<Stage>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(stage: Stage): Observable<EntityResponseType> {
        const copy = this.convert(stage);
        return this.http.put<Stage>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Stage>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Stage[]>> {
        const options = createRequestOption(req);
        return this.http.get<Stage[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Stage[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Stage[]>> {
        const options = createRequestOption(req);
        return this.http.get<Stage[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Stage[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Stage = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Stage[]>): HttpResponse<Stage[]> {
        const jsonResponse: Stage[] = res.body;
        const body: Stage[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Stage.
     */
    private convertItemFromServer(stage: Stage): Stage {
        const copy: Stage = Object.assign({}, stage);
        return copy;
    }

    /**
     * Convert a Stage to a JSON which can be sent to the server.
     */
    private convert(stage: Stage): Stage {
        const copy: Stage = Object.assign({}, stage);
        return copy;
    }
}
