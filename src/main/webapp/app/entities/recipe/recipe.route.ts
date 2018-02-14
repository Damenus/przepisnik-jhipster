import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { RecipeComponent } from './recipe.component';
import { RecipeDetailComponent } from './recipe-detail.component';
import { RecipePopupComponent } from './recipe-dialog.component';
import { RecipeDeletePopupComponent } from './recipe-delete-dialog.component';

export const recipeRoute: Routes = [
    {
        path: 'recipe',
        component: RecipeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.recipe.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'recipe/:id',
        component: RecipeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.recipe.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const recipePopupRoute: Routes = [
    {
        path: 'recipe-new',
        component: RecipePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.recipe.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'recipe/:id/edit',
        component: RecipePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.recipe.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'recipe/:id/delete',
        component: RecipeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'przepisnikApp.recipe.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
