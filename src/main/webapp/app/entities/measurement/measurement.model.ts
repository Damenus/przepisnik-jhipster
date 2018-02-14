import { BaseEntity } from './../../shared';

export class Measurement implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public amonts?: BaseEntity[],
    ) {
    }
}
