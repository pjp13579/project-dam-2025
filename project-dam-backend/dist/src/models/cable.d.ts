import mongoose, { Types } from 'mongoose';
export interface ICable {
    device1: Types.ObjectId;
    device2: Types.ObjectId;
    cableType: string;
    interface1: {
        device: Types.ObjectId;
        portId: string;
        linkSpeed: string;
        duplex: string;
    };
    interface2: {
        device: Types.ObjectId;
        portId: string;
        linkSpeed: string;
        duplex: string;
    };
}
export declare const Cable: mongoose.Model<ICable, {}, {}, {}, mongoose.Document<unknown, {}, ICable, {}, {}> & ICable & {
    _id: Types.ObjectId;
} & {
    __v: number;
}, any>;
//# sourceMappingURL=cable.d.ts.map