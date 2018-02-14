import { BaseEntity } from './../../shared';

export class Amont implements BaseEntity {
    constructor(
        public id?: number,
        public number?: number,
        public ingredientId?: number,
        public measurementId?: number,
        public stageId?: number,
    ) {
    }
}
