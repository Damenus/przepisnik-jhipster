import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrzepisnikSharedModule } from '../../shared';
import {
    MeasurementService,
    MeasurementPopupService,
    MeasurementComponent,
    MeasurementDetailComponent,
    MeasurementDialogComponent,
    MeasurementPopupComponent,
    MeasurementDeletePopupComponent,
    MeasurementDeleteDialogComponent,
    measurementRoute,
    measurementPopupRoute,
} from './';

const ENTITY_STATES = [
    ...measurementRoute,
    ...measurementPopupRoute,
];

@NgModule({
    imports: [
        PrzepisnikSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MeasurementComponent,
        MeasurementDetailComponent,
        MeasurementDialogComponent,
        MeasurementDeleteDialogComponent,
        MeasurementPopupComponent,
        MeasurementDeletePopupComponent,
    ],
    entryComponents: [
        MeasurementComponent,
        MeasurementDialogComponent,
        MeasurementPopupComponent,
        MeasurementDeleteDialogComponent,
        MeasurementDeletePopupComponent,
    ],
    providers: [
        MeasurementService,
        MeasurementPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrzepisnikMeasurementModule {}
