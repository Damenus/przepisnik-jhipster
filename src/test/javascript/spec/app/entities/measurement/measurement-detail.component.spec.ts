/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PrzepisnikTestModule } from '../../../test.module';
import { MeasurementDetailComponent } from '../../../../../../main/webapp/app/entities/measurement/measurement-detail.component';
import { MeasurementService } from '../../../../../../main/webapp/app/entities/measurement/measurement.service';
import { Measurement } from '../../../../../../main/webapp/app/entities/measurement/measurement.model';

describe('Component Tests', () => {

    describe('Measurement Management Detail Component', () => {
        let comp: MeasurementDetailComponent;
        let fixture: ComponentFixture<MeasurementDetailComponent>;
        let service: MeasurementService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [MeasurementDetailComponent],
                providers: [
                    MeasurementService
                ]
            })
            .overrideTemplate(MeasurementDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MeasurementDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MeasurementService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Measurement(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.measurement).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
