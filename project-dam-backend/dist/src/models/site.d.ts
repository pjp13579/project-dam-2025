import mongoose, { Types } from 'mongoose';
export interface IAddress {
    street: string;
    city: string;
    state: string;
    zipCode: string;
    latitude: number;
    longitude: number;
}
export interface ISite {
    _id: Types.ObjectId;
    localName: string;
    type: string;
    country: string;
    address: IAddress;
    isActive: boolean;
    createdAt: Date;
    updatedAt: Date;
    devicesAtSite: Types.ObjectId[];
}
export declare const Site: mongoose.Model<ISite, {}, {}, {}, mongoose.Document<unknown, {}, ISite, {}, {}> & ISite & Required<{
    _id: Types.ObjectId;
}> & {
    __v: number;
}, any>;
//# sourceMappingURL=site.d.ts.map