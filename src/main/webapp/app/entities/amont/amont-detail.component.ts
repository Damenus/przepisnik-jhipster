import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Amont } from './amont.model';
import { AmontService } from './amont.service';

@Component({
    selector: 'jhi-amont-detail',
    templateUrl: './amont-detail.component.html'
})
export class AmontDetailComponent implements OnInit, OnDestroy {

    amont: Amont;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private amontService: AmontService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAmonts();
    }

    load(id) {
        this.amontService.find(id)
            .subscribe((amontResponse: HttpResponse<Amont>) => {
                this.amont = amontResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAmonts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'amontListModification',
            (response) => this.load(this.amont.id)
        );
    }
}
