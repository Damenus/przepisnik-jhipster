import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { AmontComponent } from './amont.component';
import { AmontDetailComponent } from './amont-detail.component';
import { AmontPopupComponent } from './amont-dialog.component';
import { AmontDeletePopupComponent } from './amont-delete-dialog.component';

export const amontRoute: Routes = [
    {
        path: 'amont',
        component: AmontComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.amont.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'amont/:id',
        component: AmontDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.amont.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const amontPopupRoute: Routes = [
    {
        path: 'amont-new',
        component: AmontPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.amont.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'amont/:id/edit',
        component: AmontPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.amont.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'amont/:id/delete',
        component: AmontDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.amont.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
