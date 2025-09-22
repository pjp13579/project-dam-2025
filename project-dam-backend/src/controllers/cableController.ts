import {
	Controller,
	Get,
	Post,
	Put,
	Delete,
	Route,
	Query,
	Body,
	Path,
	Security,
	Tags
} from 'tsoa';
import { Cable, ICable } from '../models/cable';

interface CreateCableRequest {
	device1: string;
	device2: string;
	cableType: string;
	interface1: {
		device: string;
		portId: string;
		linkSpeed: string;
		duplex: string;
	};
	interface2: {
		device: string;
		portId: string;
		linkSpeed: string;
		duplex: string;
	};
}

interface UpdateCableRequest {
	device1?: string;
	device2?: string;
	cableType?: string;
	interface1?: {
		device?: string;
		portId?: string;
		linkSpeed?: string;
		duplex?: string;
	};
	interface2?: {
		device?: string;
		portId?: string;
		linkSpeed?: string;
		duplex?: string;
	};
}

@Route('cables')
@Tags('Cables')
@Security('jwt')
export class CableController extends Controller {
	@Get()
	public async getCables(
		@Query() page: number = 1,
		@Query() limit: number = 10
	): Promise<{ cables: ICable[]; total: number; pages: number; }> {
		const skip = (page - 1) * limit;
		const cables = await Cable.find()
			.skip(skip)
			.limit(limit)
			.sort({ createdAt: -1 })
			.populate('device1')
			.populate('device2')
			.populate('interface1.device')
			.populate('interface2.device');

		const total = await Cable.countDocuments();

		return {
			cables,
			total,
			pages: Math.ceil(total / limit)
		};
	}

	@Get('{cableId}')
	public async getCable(@Path() cableId: string): Promise<ICable> {
		const cable = await Cable.findById(cableId)
			.populate('device1')
			.populate('device2')
			.populate('interface1.device')
			.populate('interface2.device');
		if (!cable) {
			throw new Error('Cable not found');
		}
		return cable;
	}

	@Post()
	@Security('jwt', ['admin'])
	public async createCable(@Body() requestBody: CreateCableRequest): Promise<ICable> {
		try {
			const cable = new Cable(requestBody);
			await cable.save();

			const populatedCable = await Cable.findById(cable._id)
				.populate('device1')
				.populate('device2')
				.populate('interface1.device')
				.populate('interface2.device');
			if (!populatedCable) {
				throw new Error('Failed to create cable');
			}

			this.setStatus(201);
			return populatedCable;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	@Put('{cableId}')
	@Security('jwt', ['admin'])
	public async updateCable(
		@Path() cableId: string,
		@Body() requestBody: UpdateCableRequest
	): Promise<ICable> {
		try {
			const cable = await Cable.findByIdAndUpdate(
				cableId,
				requestBody,
				{ new: true, runValidators: true }
			)
				.populate('device1')
				.populate('device2')
				.populate('interface1.device')
				.populate('interface2.device');

			if (!cable) {
				throw new Error('Cable not found');
			}
			return cable;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	@Delete('{cableId}')
	@Security('jwt', ['admin'])
	public async deleteCable(@Path() cableId: string): Promise<{ message: string; }> {
		const cable = await Cable.findByIdAndDelete(cableId);
		if (!cable) {
			throw new Error('Cable not found');
		}
		return { message: 'Cable deleted successfully' };
	}
}