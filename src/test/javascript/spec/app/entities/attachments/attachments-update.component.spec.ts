/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WorkflowManagerTestModule } from '../../../test.module';
import { AttachmentsUpdateComponent } from 'app/entities/attachments/attachments-update.component';
import { AttachmentsService } from 'app/entities/attachments/attachments.service';
import { Attachments } from 'app/shared/model/attachments.model';

describe('Component Tests', () => {
    describe('Attachments Management Update Component', () => {
        let comp: AttachmentsUpdateComponent;
        let fixture: ComponentFixture<AttachmentsUpdateComponent>;
        let service: AttachmentsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [AttachmentsUpdateComponent]
            })
                .overrideTemplate(AttachmentsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AttachmentsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttachmentsService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Attachments(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.attachments = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Attachments();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.attachments = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
