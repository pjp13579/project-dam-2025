// deviceController.ts
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
import { Device, IDevice } from '../models/device';

interface GetIpRequest {
	_id: string;
	vendor: string;
	category: string;
	type: string;
	serialNumber: string;
	macAddress: string;
	state: string;
	isActive?: boolean;
}

interface CreateDeviceRequest {
	vendor: string;
	category: string;
	type: string;
	serialNumber: string;
	macAddress: string;
	state: string;
	site: string;
	connectedDevices?: string[];
	isActive?: boolean;
}

interface UpdateDeviceRequest {
	vendor?: string;
	category?: string;
	type?: string;
	serialNumber?: string;
	macAddress?: string;
	state?: string;
	site?: string;
	connectedDevices?: string[];
	isActive?: boolean;
}

@Route('devices')
@Tags('Devices')
@Security('jwt')
export class DeviceController extends Controller {
	@Get()
	public async getDevices(
		@Query() page: number = 1,
		@Query() limit: number = 10
	): Promise<{ devices: IDevice[]; total: number; pages: number; }> {
		const skip = (page - 1) * limit;
		const devices = await Device.find()
			.select('-connectedDevices')
			.skip(skip)
			.limit(limit)
			.sort({ createdAt: -1 });
			;//.populate('site')
			;//.populate('connectedDevices', '_id vendor category type serialNumber macAddress state');


		const total = await Device.countDocuments();

		return {
			devices,
			total,
			pages: Math.ceil(total / limit)
		};
	}

	@Get('{deviceId}')
	public async getDevice(@Path() deviceId: string): Promise<IDevice> {
		const device = await Device.findById(deviceId)
			.populate('site', '_id localname type country address')
			.populate('connectedDevices', '_id vendor category type serialNumber macAddress state site');
		if (!device) {
			throw new Error('Device not found');
		}
		return device;
	}

	@Post()
	@Security('jwt', ['admin'])
	public async createDevice(@Body() requestBody: CreateDeviceRequest): Promise<IDevice> {
		try {
			const device = new Device({
				...requestBody,
				createdAt: new Date(),
				updatedAt: new Date()
			});
			await device.save();

			const populatedDevice = await Device.findById(device._id)
				.populate('site')
				.populate('connectedDevices');
			if (!populatedDevice) {
				throw new Error('Failed to create device');
			}

			this.setStatus(201);
			return populatedDevice;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	@Put('{deviceId}')
	@Security('jwt', ['admin'])
	public async updateDevice(
		@Path() deviceId: string,
		@Body() requestBody: UpdateDeviceRequest
	): Promise<IDevice> {
		try {
			const device = await Device.findByIdAndUpdate(
				deviceId,
				{ ...requestBody, updatedAt: new Date() },
				{ new: true, runValidators: true }
			)
				.populate('site')
				.populate('connectedDevices');

			if (!device) {
				throw new Error('Device not found');
			}
			return device;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	@Delete('{deviceId}')
	@Security('jwt', ['admin'])
	public async deleteDevice(@Path() deviceId: string): Promise<{ message: string; }> {
		const device = await Device.findByIdAndUpdate(
			deviceId,
			{ isActive: false },
			{ new: true, runValidators: true }
		);
		if (!device) {
			throw new Error('Device not found');
		}
		return { message: 'Device deleted successfully' };
	}
}