import { Component, OnInit, Input } from '@angular/core';
import { JhiURLCasePipe } from '../../shared/string-pipes';

@Component({
    selector: 'jhi-company-block',
    templateUrl: './company-block.component.html',
    styleUrls: ['../../styles.scss'],
    providers: [JhiURLCasePipe]
})
export class JhiCompanyBlockComponent implements OnInit {
    private initialized = false;
    private companyURL: String;
    @Input() companyName = 'new';
    @Input() projectCount = 0;
    @Input() milestoneCount = 0;
    @Input() totalRevenue = 0;
    private bgColor;
    private colors;

    constructor(private urlCasePipe: JhiURLCasePipe) {}

    /**
     * Intiliaze the colour variables.
     */
    ngOnInit() {
        if (this.companyName !== 'new') {
            this.bgColor = this.getRandomColor();
            this.initialized = true;
        } else {
            this.bgColor = '#dfdfdf';
        }
        this.companyURL = this.urlCasePipe.transform(this.companyName);
    }

    /**
     * Generates a random HSL based colour. This has a bias toward darker colours as to fit
     * the contrast of the white text.
     */
    getRandomColor() {
        this.colors = [
            '#539770',
            '#4b7d74',
            '#8dc2bc',
            '#edd6b4',
            '#be7467',
            '#e2ae63',
            '#588c75',
            '#b0c47f',
            '#f3e395',
            '#f3ae73',
            '#da645a',
            '#ab5351',
            '#8d4548'
        ];
        const random = Math.floor(Math.random() * 13);
        return this.colors[random];
    }

    /**
     * Return a colour based on whether this company block has been assigned or not.
     */
    getHeadingColor() {
        if (this.initialized) {
            return 'whitesmoke';
        } else {
            return '#b7b7b7';
        }
    }
}
