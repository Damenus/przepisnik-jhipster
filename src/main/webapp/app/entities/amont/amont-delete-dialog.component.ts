import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Amont } from './amont.model';
import { AmontPopupService } from './amont-popup.service';
import { AmontService } from './amont.service';

@Component({
    selector: 'jhi-amont-delete-dialog',
    templateUrl: './amont-delete-dialog.component.html'
})
export class AmontDeleteDialogComponent {

    amont: Amont;

    constructor(
        private amontService: AmontService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.amontService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'amontListModification',
                content: 'Deleted an amont'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-amont-delete-popup',
    template: ''
})
export class AmontDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private amontPopupService: AmontPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.amontPopupService
                .open(AmontDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
