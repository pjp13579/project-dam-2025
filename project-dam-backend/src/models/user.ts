import mongoose, { Schema, Types } from 'mongoose';
import bcrypt from 'bcryptjs';

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

const userSchema = new Schema<IUser>({
	name: {
		type: String,
		required: true,
		trim: true
	},
	email: {
		type: String,
		required: true,
		unique: true,
		lowercase: true,
		trim: true
	},
	password: {
		type: String,
		required: true,
		minlength: 6
	},
	role: {
		type: String,
		enum: ['guest', 'technician', 'admin'],
		default: 'guest'
	},
	isActive: {
		type: Boolean,
		default: true
	}
},
	{
		timestamps: true, //  automatically add createdAt and updatedAt
	});

// hash password before saving
userSchema.pre('save', async function (next) {
	// avoid re-hashing password
	if (!this.isModified('password')) return next();

	try {
		const salt = await bcrypt.genSalt(10);
		this.password = await bcrypt.hash(this.password, salt);
		next();
	} catch (error: any) {
		next(error);
	}
});

// compare passwords 
userSchema.methods.comparePassword = async function (candidatePassword: string): Promise<boolean> {
	return bcrypt.compare(candidatePassword, this.password);
};

// converts MongoDB _id to string for easier client usage
userSchema.virtual('id').get(function () {
	return this._id.toHexString();
});

// ensures virtual fields (such as _id) are included when documents are serialized to JSON
userSchema.set('toJSON', {
	virtuals: true
});

export const User = mongoose.model<IUser>('User', userSchema);