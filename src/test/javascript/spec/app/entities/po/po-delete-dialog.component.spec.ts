/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WorkflowManagerTestModule } from '../../../test.module';
import { PODeleteDialogComponent } from 'app/entities/po/po-delete-dialog.component';
import { POService } from 'app/entities/po/po.service';

describe('Component Tests', () => {
    describe('PO Management Delete Component', () => {
        let comp: PODeleteDialogComponent;
        let fixture: ComponentFixture<PODeleteDialogComponent>;
        let service: POService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [PODeleteDialogComponent]
            })
                .overrideTemplate(PODeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PODeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(POService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
