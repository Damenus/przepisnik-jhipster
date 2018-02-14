import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PrzepisnikIngredientModule } from './ingredient/ingredient.module';
import { PrzepisnikMeasurementModule } from './measurement/measurement.module';
import { PrzepisnikAmontModule } from './amont/amont.module';
import { PrzepisnikStageModule } from './stage/stage.module';
import { PrzepisnikRecipeModule } from './recipe/recipe.module';
import { PrzepisnikCategoryModule } from './category/category.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PrzepisnikIngredientModule,
        PrzepisnikMeasurementModule,
        PrzepisnikAmontModule,
        PrzepisnikStageModule,
        PrzepisnikRecipeModule,
        PrzepisnikCategoryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrzepisnikEntityModule {}
