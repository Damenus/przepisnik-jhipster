/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PrzepisnikTestModule } from '../../../test.module';
import { StageComponent } from '../../../../../../main/webapp/app/entities/stage/stage.component';
import { StageService } from '../../../../../../main/webapp/app/entities/stage/stage.service';
import { Stage } from '../../../../../../main/webapp/app/entities/stage/stage.model';

describe('Component Tests', () => {

    describe('Stage Management Component', () => {
        let comp: StageComponent;
        let fixture: ComponentFixture<StageComponent>;
        let service: StageService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [StageComponent],
                providers: [
                    StageService
                ]
            })
            .overrideTemplate(StageComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StageComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StageService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Stage(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.stages[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
