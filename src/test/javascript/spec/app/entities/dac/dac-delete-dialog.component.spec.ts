/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WorkflowManagerTestModule } from '../../../test.module';
import { DACDeleteDialogComponent } from 'app/entities/dac/dac-delete-dialog.component';
import { DACService } from 'app/entities/dac/dac.service';

describe('Component Tests', () => {
    describe('DAC Management Delete Component', () => {
        let comp: DACDeleteDialogComponent;
        let fixture: ComponentFixture<DACDeleteDialogComponent>;
        let service: DACService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [DACDeleteDialogComponent]
            })
                .overrideTemplate(DACDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DACDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DACService);
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
