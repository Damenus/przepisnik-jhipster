/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PrzepisnikTestModule } from '../../../test.module';
import { AmontDetailComponent } from '../../../../../../main/webapp/app/entities/amont/amont-detail.component';
import { AmontService } from '../../../../../../main/webapp/app/entities/amont/amont.service';
import { Amont } from '../../../../../../main/webapp/app/entities/amont/amont.model';

describe('Component Tests', () => {

    describe('Amont Management Detail Component', () => {
        let comp: AmontDetailComponent;
        let fixture: ComponentFixture<AmontDetailComponent>;
        let service: AmontService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [AmontDetailComponent],
                providers: [
                    AmontService
                ]
            })
            .overrideTemplate(AmontDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AmontDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AmontService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Amont(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.amont).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
