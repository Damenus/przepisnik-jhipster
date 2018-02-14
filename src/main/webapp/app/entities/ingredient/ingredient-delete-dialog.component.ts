import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Ingredient } from './ingredient.model';
import { IngredientPopupService } from './ingredient-popup.service';
import { IngredientService } from './ingredient.service';

@Component({
    selector: 'jhi-ingredient-delete-dialog',
    templateUrl: './ingredient-delete-dialog.component.html'
})
export class IngredientDeleteDialogComponent {

    ingredient: Ingredient;

    constructor(
        private ingredientService: IngredientService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ingredientService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ingredientListModification',
                content: 'Deleted an ingredient'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ingredient-delete-popup',
    template: ''
})
export class IngredientDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ingredientPopupService: IngredientPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ingredientPopupService
                .open(IngredientDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
