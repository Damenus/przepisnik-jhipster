import { BaseEntity } from './../../shared';

export class Stage implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public text?: any,
        public amonts?: BaseEntity[],
        public recipeId?: number,
    ) {
    }
}
