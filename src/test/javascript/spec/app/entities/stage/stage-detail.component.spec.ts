/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PrzepisnikTestModule } from '../../../test.module';
import { StageDetailComponent } from '../../../../../../main/webapp/app/entities/stage/stage-detail.component';
import { StageService } from '../../../../../../main/webapp/app/entities/stage/stage.service';
import { Stage } from '../../../../../../main/webapp/app/entities/stage/stage.model';

describe('Component Tests', () => {

    describe('Stage Management Detail Component', () => {
        let comp: StageDetailComponent;
        let fixture: ComponentFixture<StageDetailComponent>;
        let service: StageService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [StageDetailComponent],
                providers: [
                    StageService
                ]
            })
            .overrideTemplate(StageDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StageDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StageService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Stage(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.stage).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
