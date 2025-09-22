import { Controller } from 'tsoa';
import { IDevice } from '../models/device';
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
export declare class DeviceController extends Controller {
    getDevices(page?: number, limit?: number): Promise<{
        devices: IDevice[];
        total: number;
        pages: number;
    }>;
    getDevice(deviceId: string): Promise<IDevice>;
    createDevice(requestBody: CreateDeviceRequest): Promise<IDevice>;
    updateDevice(deviceId: string, requestBody: UpdateDeviceRequest): Promise<IDevice>;
    deleteDevice(deviceId: string): Promise<{
        message: string;
    }>;
}
export {};
//# sourceMappingURL=deviceController.d.ts.map