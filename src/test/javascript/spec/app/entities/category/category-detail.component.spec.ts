/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PrzepisnikTestModule } from '../../../test.module';
import { CategoryDetailComponent } from '../../../../../../main/webapp/app/entities/category/category-detail.component';
import { CategoryService } from '../../../../../../main/webapp/app/entities/category/category.service';
import { Category } from '../../../../../../main/webapp/app/entities/category/category.model';

describe('Component Tests', () => {

    describe('Category Management Detail Component', () => {
        let comp: CategoryDetailComponent;
        let fixture: ComponentFixture<CategoryDetailComponent>;
        let service: CategoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [CategoryDetailComponent],
                providers: [
                    CategoryService
                ]
            })
            .overrideTemplate(CategoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CategoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CategoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Category(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.category).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
