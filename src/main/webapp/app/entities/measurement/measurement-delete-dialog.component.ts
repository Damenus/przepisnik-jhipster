import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Measurement } from './measurement.model';
import { MeasurementPopupService } from './measurement-popup.service';
import { MeasurementService } from './measurement.service';

@Component({
    selector: 'jhi-measurement-delete-dialog',
    templateUrl: './measurement-delete-dialog.component.html'
})
export class MeasurementDeleteDialogComponent {

    measurement: Measurement;

    constructor(
        private measurementService: MeasurementService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.measurementService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'measurementListModification',
                content: 'Deleted an measurement'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-measurement-delete-popup',
    template: ''
})
export class MeasurementDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private measurementPopupService: MeasurementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.measurementPopupService
                .open(MeasurementDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
