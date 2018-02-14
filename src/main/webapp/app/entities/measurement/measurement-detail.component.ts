import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Measurement } from './measurement.model';
import { MeasurementService } from './measurement.service';

@Component({
    selector: 'jhi-measurement-detail',
    templateUrl: './measurement-detail.component.html'
})
export class MeasurementDetailComponent implements OnInit, OnDestroy {

    measurement: Measurement;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private measurementService: MeasurementService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMeasurements();
    }

    load(id) {
        this.measurementService.find(id)
            .subscribe((measurementResponse: HttpResponse<Measurement>) => {
                this.measurement = measurementResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMeasurements() {
        this.eventSubscriber = this.eventManager.subscribe(
            'measurementListModification',
            (response) => this.load(this.measurement.id)
        );
    }
}
