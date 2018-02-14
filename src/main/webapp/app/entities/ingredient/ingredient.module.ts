import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrzepisnikSharedModule } from '../../shared';
import {
    IngredientService,
    IngredientPopupService,
    IngredientComponent,
    IngredientDetailComponent,
    IngredientDialogComponent,
    IngredientPopupComponent,
    IngredientDeletePopupComponent,
    IngredientDeleteDialogComponent,
    ingredientRoute,
    ingredientPopupRoute,
} from './';

const ENTITY_STATES = [
    ...ingredientRoute,
    ...ingredientPopupRoute,
];

@NgModule({
    imports: [
        PrzepisnikSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        IngredientComponent,
        IngredientDetailComponent,
        IngredientDialogComponent,
        IngredientDeleteDialogComponent,
        IngredientPopupComponent,
        IngredientDeletePopupComponent,
    ],
    entryComponents: [
        IngredientComponent,
        IngredientDialogComponent,
        IngredientPopupComponent,
        IngredientDeleteDialogComponent,
        IngredientDeletePopupComponent,
    ],
    providers: [
        IngredientService,
        IngredientPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrzepisnikIngredientModule {}
