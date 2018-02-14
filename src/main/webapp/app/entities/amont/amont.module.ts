import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrzepisnikSharedModule } from '../../shared';
import {
    AmontService,
    AmontPopupService,
    AmontComponent,
    AmontDetailComponent,
    AmontDialogComponent,
    AmontPopupComponent,
    AmontDeletePopupComponent,
    AmontDeleteDialogComponent,
    amontRoute,
    amontPopupRoute,
} from './';

const ENTITY_STATES = [
    ...amontRoute,
    ...amontPopupRoute,
];

@NgModule({
    imports: [
        PrzepisnikSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AmontComponent,
        AmontDetailComponent,
        AmontDialogComponent,
        AmontDeleteDialogComponent,
        AmontPopupComponent,
        AmontDeletePopupComponent,
    ],
    entryComponents: [
        AmontComponent,
        AmontDialogComponent,
        AmontPopupComponent,
        AmontDeleteDialogComponent,
        AmontDeletePopupComponent,
    ],
    providers: [
        AmontService,
        AmontPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrzepisnikAmontModule {}
