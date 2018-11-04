/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkflowManagerTestModule } from '../../../test.module';
import { AttachmentsDetailComponent } from 'app/entities/attachments/attachments-detail.component';
import { Attachments } from 'app/shared/model/attachments.model';

describe('Component Tests', () => {
    describe('Attachments Management Detail Component', () => {
        let comp: AttachmentsDetailComponent;
        let fixture: ComponentFixture<AttachmentsDetailComponent>;
        const route = ({ data: of({ attachments: new Attachments(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [AttachmentsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AttachmentsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AttachmentsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.attachments).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
