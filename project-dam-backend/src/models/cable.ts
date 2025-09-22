import mongoose, { Schema, Types, Document } from 'mongoose';

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

const CableSchema = new Schema<ICable>({
	device1: {
		type: Schema.Types.ObjectId,
		ref: 'Device',
		required: true
	},
	device2: {
		type: Schema.Types.ObjectId,
		ref: 'Device',
		required: true
	},
	cableType: { type: String, required: true },
	interface1: {
		device: {
			type: Schema.Types.ObjectId,
			ref: 'Device',
			required: true
		},
		portId: { type: String, required: true },
		linkSpeed: { type: String, required: true },
		duplex: { type: String, required: true }
	},
	interface2: {
		device: {
			type: Schema.Types.ObjectId,
			ref: 'Device',
			required: true
		},
		portId: { type: String, required: true },
		linkSpeed: { type: String, required: true },
		duplex: { type: String, required: true }
	}
});

export const Cable = mongoose.model<ICable>('Cable', CableSchema);