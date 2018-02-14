import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Ingredient } from './ingredient.model';
import { IngredientPopupService } from './ingredient-popup.service';
import { IngredientService } from './ingredient.service';

@Component({
    selector: 'jhi-ingredient-dialog',
    templateUrl: './ingredient-dialog.component.html'
})
export class IngredientDialogComponent implements OnInit {

    ingredient: Ingredient;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private ingredientService: IngredientService,
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
        if (this.ingredient.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ingredientService.update(this.ingredient));
        } else {
            this.subscribeToSaveResponse(
                this.ingredientService.create(this.ingredient));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Ingredient>>) {
        result.subscribe((res: HttpResponse<Ingredient>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Ingredient) {
        this.eventManager.broadcast({ name: 'ingredientListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-ingredient-popup',
    template: ''
})
export class IngredientPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ingredientPopupService: IngredientPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ingredientPopupService
                    .open(IngredientDialogComponent as Component, params['id']);
            } else {
                this.ingredientPopupService
                    .open(IngredientDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
