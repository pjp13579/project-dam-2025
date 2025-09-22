import mongoose, { Schema, Types, Document } from 'mongoose';

// Base interface without mongoose Document
export interface IIp  {
	_id: Types.ObjectId;
	ip: string;
	mask: number;
	type: string;
	logicalEntity: string;
	createdAt: Date;
	updatedAt: Date;
	isActive: { type: boolean, default: true },
	devices: Types.ObjectId[]; // 1-M with sites
}


const IpSchema = new Schema<IIp>({
	ip: { type: String, required: true },
	mask: { type: Number, required: true },
	type: { type: String, required: true },
	logicalEntity: { type: String, required: true },
	isActive: { type: Boolean, default: true },
	devices: [{ type: Schema.Types.ObjectId, ref: 'device' }] // 1-M with sites
},
	{ timestamps: true } // automatically add createdAt and updatedAt
);

export const Ip = mongoose.model<IIp>('Ip', IpSchema);
