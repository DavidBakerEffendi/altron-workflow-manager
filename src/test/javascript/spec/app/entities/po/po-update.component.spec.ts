/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WorkflowManagerTestModule } from '../../../test.module';
import { POUpdateComponent } from 'app/entities/po/po-update.component';
import { POService } from 'app/entities/po/po.service';
import { PO } from 'app/shared/model/po.model';

describe('Component Tests', () => {
    describe('PO Management Update Component', () => {
        let comp: POUpdateComponent;
        let fixture: ComponentFixture<POUpdateComponent>;
        let service: POService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [POUpdateComponent]
            })
                .overrideTemplate(POUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(POUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(POService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PO(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.pO = entity;
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
                    const entity = new PO();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.pO = entity;
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
