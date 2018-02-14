/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrzepisnikTestModule } from '../../../test.module';
import { IngredientComponent } from '../../../../../../main/webapp/app/entities/ingredient/ingredient.component';
import { IngredientService } from '../../../../../../main/webapp/app/entities/ingredient/ingredient.service';
import { Ingredient } from '../../../../../../main/webapp/app/entities/ingredient/ingredient.model';

describe('Component Tests', () => {

    describe('Ingredient Management Component', () => {
        let comp: IngredientComponent;
        let fixture: ComponentFixture<IngredientComponent>;
        let service: IngredientService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [IngredientComponent],
                providers: [
                    IngredientService
                ]
            })
            .overrideTemplate(IngredientComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IngredientComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IngredientService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Ingredient(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.ingredients[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
