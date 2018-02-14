import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Amont } from './amont.model';
import { AmontPopupService } from './amont-popup.service';
import { AmontService } from './amont.service';
import { Ingredient, IngredientService } from '../ingredient';
import { Measurement, MeasurementService } from '../measurement';
import { Stage, StageService } from '../stage';

@Component({
    selector: 'jhi-amont-dialog',
    templateUrl: './amont-dialog.component.html'
})
export class AmontDialogComponent implements OnInit {

    amont: Amont;
    isSaving: boolean;

    ingredients: Ingredient[];

    measurements: Measurement[];

    stages: Stage[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private amontService: AmontService,
        private ingredientService: IngredientService,
        private measurementService: MeasurementService,
        private stageService: StageService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.ingredientService.query()
            .subscribe((res: HttpResponse<Ingredient[]>) => { this.ingredients = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.measurementService.query()
            .subscribe((res: HttpResponse<Measurement[]>) => { this.measurements = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.stageService.query()
            .subscribe((res: HttpResponse<Stage[]>) => { this.stages = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.amont.id !== undefined) {
            this.subscribeToSaveResponse(
                this.amontService.update(this.amont));
        } else {
            this.subscribeToSaveResponse(
                this.amontService.create(this.amont));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Amont>>) {
        result.subscribe((res: HttpResponse<Amont>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Amont) {
        this.eventManager.broadcast({ name: 'amontListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackIngredientById(index: number, item: Ingredient) {
        return item.id;
    }

    trackMeasurementById(index: number, item: Measurement) {
        return item.id;
    }

    trackStageById(index: number, item: Stage) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-amont-popup',
    template: ''
})
export class AmontPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private amontPopupService: AmontPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.amontPopupService
                    .open(AmontDialogComponent as Component, params['id']);
            } else {
                this.amontPopupService
                    .open(AmontDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
