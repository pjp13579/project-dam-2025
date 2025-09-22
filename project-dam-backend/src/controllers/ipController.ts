// ipController.ts
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
import { Ip, IIp } from '../models/ip';

interface CreateIpRequest {
	ip: string;
	mask: number;
	type: string;
	logicalEntity: string;
	isActive?: boolean;
	devices?: string[];
}

interface UpdateIpRequest {
	ip?: string;
	mask?: number;
	type?: string;
	logicalEntity?: string;
	isActive?: boolean;
	devices?: string[];
}

@Route('ips')
@Tags('IPs')
@Security('jwt')
export class IpController extends Controller {
	@Get()
	public async getIps(
		@Query() page: number = 1,
		@Query() limit: number = 10
	): Promise<{ ips: IIp[]; total: number; pages: number; }> {
		const skip = (page - 1) * limit;
		const ips = await Ip.find()
			.skip(skip)
			.limit(limit)
			.sort({ createdAt: -1 })
			.populate('devices');

		const total = await Ip.countDocuments();

		return {
			ips,
			total,
			pages: Math.ceil(total / limit)
		};
	}

	@Get('{ipId}')
	public async getIp(@Path() ipId: string): Promise<IIp> {
		const ip = await Ip.findById(ipId).populate('devices');
		if (!ip) {
			throw new Error('IP not found');
		}
		return ip;
	}

	@Post()
	@Security('jwt', ['admin'])
	public async createIp(@Body() requestBody: CreateIpRequest): Promise<IIp> {
		try {
			const ip = new Ip({
				...requestBody,
				createdAt: new Date(),
				updatedAt: new Date()
			});
			await ip.save();

			const populatedIp = await Ip.findById(ip._id).populate('devices');
			if (!populatedIp) {
				throw new Error('Failed to create IP');
			}

			this.setStatus(201);
			return populatedIp;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	@Put('{ipId}')
	@Security('jwt', ['admin'])
	public async updateIp(
		@Path() ipId: string,
		@Body() requestBody: UpdateIpRequest
	): Promise<IIp> {
		try {
			const ip = await Ip.findByIdAndUpdate(
				ipId,
				{ ...requestBody, updatedAt: new Date() },
				{ new: true, runValidators: true }
			).populate('devices');

			if (!ip) {
				throw new Error('IP not found');
			}
			return ip;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	@Delete('{ipId}')
	@Security('jwt', ['admin'])
	public async deleteIp(@Path() ipId: string): Promise<{ message: string; }> {
		const ip = await Ip.findByIdAndDelete(ipId);
		if (!ip) {
			throw new Error('IP not found');
		}
		return { message: 'IP deleted successfully' };
	}
}