import { Component, Input, OnInit } from '@angular/core';
import { IProject } from 'app/shared/model/project.model';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';

@Component({
    selector: 'jhi-project-milestone',
    templateUrl: './project-milestone.component.html'
})
export class JhiProjectMilestoneComponent implements OnInit {
    @Input() project: IProject;
    @Input() companyUrl: String;
    milestones: Object[];

    constructor(private http: HttpClient) {}

    ngOnInit() {
        this.getMilestones();
    }

    /**
     * Get's the milestones associated with this project.
     */
    getMilestones() {
        this.http.get(SERVER_API_URL + 'api/project/get-' + this.project.id + '-milestones').subscribe(
            (res: Object[]) => {
                this.milestones = res;
            },
            err => {
                console.log(err);
                console.log(this.project.milestones);
            }
        );
    }
}
