import mongoose, { Schema, Types, Document } from 'mongoose';

export interface IDevice  {
	vendor: string;
	category: string;
	type: string;
	serialNumber: string;
	macAddress: string;
	state: string;
	site: Types.ObjectId; // M-1 with Site
	connectedDevices: Types.ObjectId[]; // M-M with Device
	isActive: boolean;

}

const DeviceSchema = new Schema<IDevice>({
	vendor: { type: String, required: true },
	category: { type: String, required: true },
	type: { type: String, required: true },
	serialNumber: { type: String, required: true, unique: true },
	macAddress: { type: String, required: true },
	state: { type: String, required: true },
	site: { type: Schema.Types.ObjectId, ref: 'Site' }, // M-1
	connectedDevices: [{ type: Schema.Types.ObjectId, ref: 'Device' }], // M-M
  	isActive: { type: Boolean, default: true }
});

export const Device = mongoose.model<IDevice>('Device', DeviceSchema);