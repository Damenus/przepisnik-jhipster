import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Stage } from './stage.model';
import { StagePopupService } from './stage-popup.service';
import { StageService } from './stage.service';
import { Recipe, RecipeService } from '../recipe';

@Component({
    selector: 'jhi-stage-dialog',
    templateUrl: './stage-dialog.component.html'
})
export class StageDialogComponent implements OnInit {

    stage: Stage;
    isSaving: boolean;

    recipes: Recipe[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private stageService: StageService,
        private recipeService: RecipeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.recipeService.query()
            .subscribe((res: HttpResponse<Recipe[]>) => { this.recipes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.stage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.stageService.update(this.stage));
        } else {
            this.subscribeToSaveResponse(
                this.stageService.create(this.stage));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Stage>>) {
        result.subscribe((res: HttpResponse<Stage>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Stage) {
        this.eventManager.broadcast({ name: 'stageListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackRecipeById(index: number, item: Recipe) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-stage-popup',
    template: ''
})
export class StagePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stagePopupService: StagePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.stagePopupService
                    .open(StageDialogComponent as Component, params['id']);
            } else {
                this.stagePopupService
                    .open(StageDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
