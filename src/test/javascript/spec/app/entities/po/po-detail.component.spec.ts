/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkflowManagerTestModule } from '../../../test.module';
import { PODetailComponent } from 'app/entities/po/po-detail.component';
import { PO } from 'app/shared/model/po.model';

describe('Component Tests', () => {
    describe('PO Management Detail Component', () => {
        let comp: PODetailComponent;
        let fixture: ComponentFixture<PODetailComponent>;
        const route = ({ data: of({ pO: new PO(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [PODetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PODetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PODetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.pO).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
