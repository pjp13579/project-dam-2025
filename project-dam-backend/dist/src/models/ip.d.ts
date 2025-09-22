import mongoose, { Types } from 'mongoose';
export interface IIp {
    _id: Types.ObjectId;
    ip: string;
    mask: number;
    type: string;
    logicalEntity: string;
    createdAt: Date;
    updatedAt: Date;
    isActive: {
        type: boolean;
        default: true;
    };
    devices: Types.ObjectId[];
}
export declare const Ip: mongoose.Model<IIp, {}, {}, {}, mongoose.Document<unknown, {}, IIp, {}, {}> & IIp & Required<{
    _id: Types.ObjectId;
}> & {
    __v: number;
}, any>;
//# sourceMappingURL=ip.d.ts.map