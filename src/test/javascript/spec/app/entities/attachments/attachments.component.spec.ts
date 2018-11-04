/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WorkflowManagerTestModule } from '../../../test.module';
import { AttachmentsComponent } from 'app/entities/attachments/attachments.component';
import { AttachmentsService } from 'app/entities/attachments/attachments.service';
import { Attachments } from 'app/shared/model/attachments.model';

describe('Component Tests', () => {
    describe('Attachments Management Component', () => {
        let comp: AttachmentsComponent;
        let fixture: ComponentFixture<AttachmentsComponent>;
        let service: AttachmentsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkflowManagerTestModule],
                declarations: [AttachmentsComponent],
                providers: []
            })
                .overrideTemplate(AttachmentsComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AttachmentsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttachmentsService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Attachments(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.attachments[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
