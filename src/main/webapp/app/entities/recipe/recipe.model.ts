import { BaseEntity } from './../../shared';

export const enum Rating {
    'ONE',
    'TWO',
    'THREE',
    'FOUR',
    'FIVE'
}

export class Recipe implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public description?: any,
        public extra?: any,
        public imageContentType?: string,
        public image?: any,
        public rating?: Rating,
        public stages?: BaseEntity[],
        public categories?: BaseEntity[],
    ) {
    }
}
