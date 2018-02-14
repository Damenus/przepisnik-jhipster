/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrzepisnikTestModule } from '../../../test.module';
import { AmontComponent } from '../../../../../../main/webapp/app/entities/amont/amont.component';
import { AmontService } from '../../../../../../main/webapp/app/entities/amont/amont.service';
import { Amont } from '../../../../../../main/webapp/app/entities/amont/amont.model';

describe('Component Tests', () => {

    describe('Amont Management Component', () => {
        let comp: AmontComponent;
        let fixture: ComponentFixture<AmontComponent>;
        let service: AmontService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [AmontComponent],
                providers: [
                    AmontService
                ]
            })
            .overrideTemplate(AmontComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AmontComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AmontService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Amont(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.amonts[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
