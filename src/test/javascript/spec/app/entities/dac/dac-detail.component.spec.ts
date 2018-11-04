/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkflowManagerTestModule } from '../../../test.module';
import { DACDetailComponent } from 'app/entities/dac/dac-detail.component';
import { DAC } from 'app/shared/model/dac.model';

describe('Component Tests', () => {
    describe('DAC Management Detail Component', () => {
        let comp: DACDetailComponent;
        let fixture: ComponentFixture<DACDetailComponent>;
        const route = ({ data: of({ dAC: new DAC(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [DACDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DACDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DACDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.dAC).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
