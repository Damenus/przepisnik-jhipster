import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Ingredient } from './ingredient.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Ingredient>;

@Injectable()
export class IngredientService {

    private resourceUrl =  SERVER_API_URL + 'api/ingredients';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/ingredients';

    constructor(private http: HttpClient) { }

    create(ingredient: Ingredient): Observable<EntityResponseType> {
        const copy = this.convert(ingredient);
        return this.http.post<Ingredient>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(ingredient: Ingredient): Observable<EntityResponseType> {
        const copy = this.convert(ingredient);
        return this.http.put<Ingredient>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Ingredient>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Ingredient[]>> {
        const options = createRequestOption(req);
        return this.http.get<Ingredient[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Ingredient[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Ingredient[]>> {
        const options = createRequestOption(req);
        return this.http.get<Ingredient[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Ingredient[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Ingredient = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Ingredient[]>): HttpResponse<Ingredient[]> {
        const jsonResponse: Ingredient[] = res.body;
        const body: Ingredient[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Ingredient.
     */
    private convertItemFromServer(ingredient: Ingredient): Ingredient {
        const copy: Ingredient = Object.assign({}, ingredient);
        return copy;
    }

    /**
     * Convert a Ingredient to a JSON which can be sent to the server.
     */
    private convert(ingredient: Ingredient): Ingredient {
        const copy: Ingredient = Object.assign({}, ingredient);
        return copy;
    }
}
