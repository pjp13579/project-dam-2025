import mongoose, { Schema, Types, Document } from 'mongoose';

// Define interface for Address
export interface IAddress {
	street: string;
	city: string;
	state: string;
	zipCode: string;
	latitude: number;
	longitude: number;
}

const AddressSchema = new Schema<IAddress>({
	street: String,
	city: String,
	state: String,
	zipCode: String,
	latitude: Number,
	longitude: Number
});

// Update ISite interface to use IAddress
export interface ISite {
	_id: Types.ObjectId;
	localName: string;
	type: string;
	country: string;
	address: IAddress;  // Changed to use interface
	isActive: boolean;
	createdAt: Date;
	updatedAt: Date;
	devicesAtSite: Types.ObjectId[];
}

const SiteSchema = new Schema<ISite>({
	localName: { type: String, required: true },
	type: { type: String, required: true },
	country: { type: String, required: true },
	address: AddressSchema,
	isActive: { type: Boolean, default: true },
	devicesAtSite: [{ type: Schema.Types.ObjectId, ref: 'Device' }]
}, { timestamps: true });

export const Site = mongoose.model<ISite>('Site', SiteSchema);