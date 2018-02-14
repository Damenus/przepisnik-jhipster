import { BaseEntity } from './../../shared';

export const enum Rating {
    '1',
    '2',
    '3',
    '4',
    '5'
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
