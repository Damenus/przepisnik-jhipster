import { BaseEntity } from './../../shared';

export class Ingredient implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public amonts?: BaseEntity[],
    ) {
    }
}
