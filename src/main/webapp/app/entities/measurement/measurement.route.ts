import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { MeasurementComponent } from './measurement.component';
import { MeasurementDetailComponent } from './measurement-detail.component';
import { MeasurementPopupComponent } from './measurement-dialog.component';
import { MeasurementDeletePopupComponent } from './measurement-delete-dialog.component';

export const measurementRoute: Routes = [
    {
        path: 'measurement',
        component: MeasurementComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.measurement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'measurement/:id',
        component: MeasurementDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.measurement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const measurementPopupRoute: Routes = [
    {
        path: 'measurement-new',
        component: MeasurementPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.measurement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'measurement/:id/edit',
        component: MeasurementPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.measurement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'measurement/:id/delete',
        component: MeasurementDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.measurement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
