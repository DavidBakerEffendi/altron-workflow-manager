import { NgModule } from '@angular/core';
import { JhiTitleCasePipe, JhiURLCasePipe, JhiRandCasePipe } from './string-pipes';

@NgModule({
    imports: [],
    declarations: [JhiTitleCasePipe, JhiURLCasePipe, JhiRandCasePipe],
    exports: [JhiTitleCasePipe, JhiURLCasePipe, JhiRandCasePipe]
})
export class PipeModule {
    static forRoot() {
        return {
            ngModule: PipeModule,
            providers: []
        };
    }
}
