import mongoose, { Types } from 'mongoose';
export interface IDevice {
    vendor: string;
    category: string;
    type: string;
    serialNumber: string;
    macAddress: string;
    state: string;
    site: Types.ObjectId;
    connectedDevices: Types.ObjectId[];
    isActive: boolean;
}
export declare const Device: mongoose.Model<IDevice, {}, {}, {}, mongoose.Document<unknown, {}, IDevice, {}, {}> & IDevice & {
    _id: Types.ObjectId;
} & {
    __v: number;
}, any>;
//# sourceMappingURL=device.d.ts.map