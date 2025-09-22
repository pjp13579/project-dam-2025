import { Controller } from 'tsoa';
import { IIp } from '../models/ip';
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
export declare class IpController extends Controller {
    getIps(page?: number, limit?: number): Promise<{
        ips: IIp[];
        total: number;
        pages: number;
    }>;
    getIp(ipId: string): Promise<IIp>;
    createIp(requestBody: CreateIpRequest): Promise<IIp>;
    updateIp(ipId: string, requestBody: UpdateIpRequest): Promise<IIp>;
    deleteIp(ipId: string): Promise<{
        message: string;
    }>;
}
export {};
//# sourceMappingURL=ipController.d.ts.map