/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WorkflowManagerTestModule } from '../../../test.module';
import { DACUpdateComponent } from 'app/entities/dac/dac-update.component';
import { DACService } from 'app/entities/dac/dac.service';
import { DAC } from 'app/shared/model/dac.model';

describe('Component Tests', () => {
    describe('DAC Management Update Component', () => {
        let comp: DACUpdateComponent;
        let fixture: ComponentFixture<DACUpdateComponent>;
        let service: DACService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [DACUpdateComponent]
            })
                .overrideTemplate(DACUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DACUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DACService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new DAC(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.dAC = entity;
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
                    const entity = new DAC();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.dAC = entity;
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
