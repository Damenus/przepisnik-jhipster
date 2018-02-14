/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PrzepisnikTestModule } from '../../../test.module';
import { IngredientDetailComponent } from '../../../../../../main/webapp/app/entities/ingredient/ingredient-detail.component';
import { IngredientService } from '../../../../../../main/webapp/app/entities/ingredient/ingredient.service';
import { Ingredient } from '../../../../../../main/webapp/app/entities/ingredient/ingredient.model';

describe('Component Tests', () => {

    describe('Ingredient Management Detail Component', () => {
        let comp: IngredientDetailComponent;
        let fixture: ComponentFixture<IngredientDetailComponent>;
        let service: IngredientService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [IngredientDetailComponent],
                providers: [
                    IngredientService
                ]
            })
            .overrideTemplate(IngredientDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IngredientDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IngredientService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Ingredient(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.ingredient).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
