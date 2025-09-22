import mongoose, { Types } from 'mongoose';
export interface IUser {
    _id: Types.ObjectId;
    name: string;
    email: string;
    password: string;
    role: 'guest' | 'technician' | 'admin';
    isActive: boolean;
    createdAt: Date;
    updatedAt: Date;
    comparePassword(candidatePassword: string): Promise<boolean>;
}
export declare const User: mongoose.Model<IUser, {}, {}, {}, mongoose.Document<unknown, {}, IUser, {}, {}> & IUser & Required<{
    _id: Types.ObjectId;
}> & {
    __v: number;
}, any>;
//# sourceMappingURL=user.d.ts.map