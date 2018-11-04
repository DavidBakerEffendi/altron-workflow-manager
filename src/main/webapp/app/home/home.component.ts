import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService, Principal, Account } from 'app/core';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['../styles.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    companies: Object[];
    statistics: Object[];
    selectedFile: File = null;
    hideLoginAlert = true;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private http: HttpClient
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        if (this.isAuthenticated) {
            this.displayCompanies();
            this.displayStatistics();
            this.fadeOutLoginNotification();
        }
    }

    /**
     * If the authentication was a success, register the result with the event manager and initialize the user account.
     */
    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
                if (this.isAuthenticated) {
                    this.displayCompanies();
                    this.displayStatistics();
                    this.fadeOutLoginNotification();
                }
            });
        });
    }

    /**
     * Checks if the user is authenticated.
     */
    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    /**
     * Opens the login modal and waits for a login attempt.
     */
    login() {
        this.modalRef = this.loginModalService.open();
    }

    /**
     * Performs a GET request to the API in order to return a list of all the companies and their details.
     */
    displayCompanies() {
        this.http.get(SERVER_API_URL + 'api/dashboard/get-companies').subscribe(
            (res: Object[]) => {
                this.companies = res;
            },
            err => {
                console.log(err);
            }
        );
    }

    /**
     * Handles a file being selected for upload.
     *
     * @param event The file input event.
     */
    onFileSelected(event) {
        this.selectedFile = <File>event.target.files[0];
    }

    /**
     * Uploads the selected file.
     */
    onUpload() {
        if (this.selectedFile != null) {
            const fd = new FormData();
            fd.append('file', this.selectedFile, this.selectedFile.name);
            this.http.post(SERVER_API_URL + 'api/dashboard/upload-file', fd).subscribe(res => {
                console.log('Upload Response:');
                console.log(res);
            });
        } else {
            console.error('No file selected!');
        }
    }

    /**
     * Sends a test notification email to the currently logged on user.
     */
    sendTestEmail() {
        this.http.get(SERVER_API_URL + 'api/dashboard/test-email').subscribe(
            res => {
                console.log(res);
            },
            err => {
                console.log(err);
            }
        );
    }

    /**
     * Sets this.hideLoginAlert to false after a 2 second delay once called.
     */
    fadeOutLoginNotification() {
        setTimeout(() => {
            this.hideLoginAlert = false;
        }, 2000);
    }

    /**
     * Obtains database statistics from the server.
     */
    displayStatistics() {
        this.http.get(SERVER_API_URL + 'api/statistics/dashboard-stats').subscribe(
            (res: Object[]) => {
                this.statistics = res;
            },
            err => {
                console.log(err);
            }
        );
    }
}
