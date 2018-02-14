import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Stage } from './stage.model';
import { StageService } from './stage.service';

@Component({
    selector: 'jhi-stage-detail',
    templateUrl: './stage-detail.component.html'
})
export class StageDetailComponent implements OnInit, OnDestroy {

    stage: Stage;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private stageService: StageService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStages();
    }

    load(id) {
        this.stageService.find(id)
            .subscribe((stageResponse: HttpResponse<Stage>) => {
                this.stage = stageResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'stageListModification',
            (response) => this.load(this.stage.id)
        );
    }
}
