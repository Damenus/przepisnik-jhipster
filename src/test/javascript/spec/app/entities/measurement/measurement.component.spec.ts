/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrzepisnikTestModule } from '../../../test.module';
import { MeasurementComponent } from '../../../../../../main/webapp/app/entities/measurement/measurement.component';
import { MeasurementService } from '../../../../../../main/webapp/app/entities/measurement/measurement.service';
import { Measurement } from '../../../../../../main/webapp/app/entities/measurement/measurement.model';

describe('Component Tests', () => {

    describe('Measurement Management Component', () => {
        let comp: MeasurementComponent;
        let fixture: ComponentFixture<MeasurementComponent>;
        let service: MeasurementService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [MeasurementComponent],
                providers: [
                    MeasurementService
                ]
            })
            .overrideTemplate(MeasurementComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MeasurementComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MeasurementService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Measurement(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.measurements[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
