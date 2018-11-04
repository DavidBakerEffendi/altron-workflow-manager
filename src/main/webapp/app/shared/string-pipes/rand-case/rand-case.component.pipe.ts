import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'convertFromCentsToRands' })
export class JhiRandCasePipe implements PipeTransform {
    transform(cents: number) {
        if (!cents) {
            return 'R 0.00';
        }
        /* Convert from cents to rands */
        const inRands = cents / 100.0;
        let ret = '';
        let addMilion = false;
        let stringRep: String;
        /* Adds  M at end if amount is large enough */
        if (inRands >= 1000000) {
            stringRep = String((inRands / 1000000.0).toFixed(2));
            addMilion = true;
        } else {
            stringRep = String(inRands.toFixed(2));
        }
        if (stringRep.charAt(0) === '-') {
            stringRep = stringRep.substring(1, stringRep.length);
            ret += '-';
        }
        ret += 'R ';
        /* Adds the first, non full set digits to the return string */
        if (stringRep.length % 3 === 1) {
            ret += stringRep.substring(0, 1);
            stringRep = stringRep.substring(1, stringRep.length);
        } else if (stringRep.length % 3 === 2) {
            ret += stringRep.substring(0, 2);
            stringRep = stringRep.substring(2, stringRep.length);
        }

        for (let i = 0; i < stringRep.length - 3; i += 3) {
            ret += ' ' + stringRep.substr(i, 3);
        }
        /* The final 3 characters will always be .xx from .toFixed(2) */
        ret += stringRep.substr(stringRep.length - 3, 3);
        if (addMilion) {
            ret += 'M';
        }
        return ret;
    }
}
