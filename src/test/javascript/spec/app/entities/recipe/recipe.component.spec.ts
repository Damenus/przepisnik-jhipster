/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrzepisnikTestModule } from '../../../test.module';
import { RecipeComponent } from '../../../../../../main/webapp/app/entities/recipe/recipe.component';
import { RecipeService } from '../../../../../../main/webapp/app/entities/recipe/recipe.service';
import { Recipe } from '../../../../../../main/webapp/app/entities/recipe/recipe.model';

describe('Component Tests', () => {

    describe('Recipe Management Component', () => {
        let comp: RecipeComponent;
        let fixture: ComponentFixture<RecipeComponent>;
        let service: RecipeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [RecipeComponent],
                providers: [
                    RecipeService
                ]
            })
            .overrideTemplate(RecipeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RecipeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RecipeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Recipe(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.recipes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
