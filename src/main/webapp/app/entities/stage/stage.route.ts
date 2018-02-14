import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { StageComponent } from './stage.component';
import { StageDetailComponent } from './stage-detail.component';
import { StagePopupComponent } from './stage-dialog.component';
import { StageDeletePopupComponent } from './stage-delete-dialog.component';

export const stageRoute: Routes = [
    {
        path: 'stage',
        component: StageComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.stage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'stage/:id',
        component: StageDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.stage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const stagePopupRoute: Routes = [
    {
        path: 'stage-new',
        component: StagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.stage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'stage/:id/edit',
        component: StagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.stage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'stage/:id/delete',
        component: StageDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.stage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
