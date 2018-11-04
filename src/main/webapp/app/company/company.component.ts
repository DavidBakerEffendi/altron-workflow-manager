import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { JhiTitleCasePipe } from '../shared/string-pipes';
import { Principal, Account } from 'app/core';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { ICompany } from 'app/shared/model/company.model';
import { IProject } from 'app/shared/model/project.model';

@Component({
    selector: 'jhi-company',
    templateUrl: './company.component.html',
    styleUrls: ['../styles.scss'],
    providers: [JhiTitleCasePipe]
})
export class CompanyComponent {
    account: Account;
    private statistics: Object[];
    private company: ICompany;
    private projects: IProject[];

    constructor(
        private route: ActivatedRoute,
        private titleService: Title,
        private titleCasePipe: JhiTitleCasePipe,
        private http: HttpClient,
        private principal: Principal,
        private router: Router
    ) {
        this.route.params.subscribe(params => {
            if (!this.isAuthenticated()) {
                router.navigate(['#']);
            }
            this.http.get(SERVER_API_URL + 'api/company/get-' + params['company'].toLowerCase()).subscribe(
                res => {
                    if (res === null) {
                        this.previousState();
                    } else {
                        this.company = res;
                        this.titleService.setTitle(this.titleCasePipe.transform(this.company.companyName) + ' | Altron Workflow Manager');
                        this.getStatistics();
                        this.getProjects();
                    }
                },
                err => {
                    console.log(err);
                    this.previousState();
                }
            );
        });
    }

    /**
     * Obtains database statistics from the server.
     */
    getStatistics() {
        this.http.get(SERVER_API_URL + 'api/statistics/company-stats/' + this.company.companyName.toLowerCase()).subscribe(
            (res: Object[]) => {
                this.statistics = res;
            },
            err => {
                console.log(err);
            }
        );
    }

    /**
     * Gets the projects associated with this company.
     */
    getProjects() {
        this.http.get(SERVER_API_URL + 'api/project/get-' + this.company.companyName.toLowerCase() + '-projects').subscribe(
            (res: IProject[]) => {
                this.projects = res;
            },
            err => {
                console.log(err);
            }
        );
    }

    /**
     * Checks if the user is authenticated.
     */
    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    previousState() {
        window.history.back();
    }
}
