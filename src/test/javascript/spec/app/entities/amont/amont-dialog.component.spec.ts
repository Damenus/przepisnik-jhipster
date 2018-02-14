/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PrzepisnikTestModule } from '../../../test.module';
import { AmontDialogComponent } from '../../../../../../main/webapp/app/entities/amont/amont-dialog.component';
import { AmontService } from '../../../../../../main/webapp/app/entities/amont/amont.service';
import { Amont } from '../../../../../../main/webapp/app/entities/amont/amont.model';
import { IngredientService } from '../../../../../../main/webapp/app/entities/ingredient';
import { MeasurementService } from '../../../../../../main/webapp/app/entities/measurement';
import { StageService } from '../../../../../../main/webapp/app/entities/stage';

describe('Component Tests', () => {

    describe('Amont Management Dialog Component', () => {
        let comp: AmontDialogComponent;
        let fixture: ComponentFixture<AmontDialogComponent>;
        let service: AmontService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrzepisnikTestModule],
                declarations: [AmontDialogComponent],
                providers: [
                    IngredientService,
                    MeasurementService,
                    StageService,
                    AmontService
                ]
            })
            .overrideTemplate(AmontDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AmontDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AmontService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Amont(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.amont = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'amontListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Amont();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.amont = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'amontListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
