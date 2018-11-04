/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WorkflowManagerTestModule } from '../../../test.module';
import { AttachmentsDeleteDialogComponent } from 'app/entities/attachments/attachments-delete-dialog.component';
import { AttachmentsService } from 'app/entities/attachments/attachments.service';

describe('Component Tests', () => {
    describe('Attachments Management Delete Component', () => {
        let comp: AttachmentsDeleteDialogComponent;
        let fixture: ComponentFixture<AttachmentsDeleteDialogComponent>;
        let service: AttachmentsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [AttachmentsDeleteDialogComponent]
            })
                .overrideTemplate(AttachmentsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AttachmentsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttachmentsService);
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
