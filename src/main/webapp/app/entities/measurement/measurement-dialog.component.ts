import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Measurement } from './measurement.model';
import { MeasurementPopupService } from './measurement-popup.service';
import { MeasurementService } from './measurement.service';

@Component({
    selector: 'jhi-measurement-dialog',
    templateUrl: './measurement-dialog.component.html'
})
export class MeasurementDialogComponent implements OnInit {

    measurement: Measurement;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private measurementService: MeasurementService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.measurement.id !== undefined) {
            this.subscribeToSaveResponse(
                this.measurementService.update(this.measurement));
        } else {
            this.subscribeToSaveResponse(
                this.measurementService.create(this.measurement));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Measurement>>) {
        result.subscribe((res: HttpResponse<Measurement>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Measurement) {
        this.eventManager.broadcast({ name: 'measurementListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-measurement-popup',
    template: ''
})
export class MeasurementPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private measurementPopupService: MeasurementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.measurementPopupService
                    .open(MeasurementDialogComponent as Component, params['id']);
            } else {
                this.measurementPopupService
                    .open(MeasurementDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
