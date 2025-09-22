import { Controller } from 'tsoa';
import { ICable } from '../models/cable';
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
export declare class CableController extends Controller {
    getCables(page?: number, limit?: number): Promise<{
        cables: ICable[];
        total: number;
        pages: number;
    }>;
    getCable(cableId: string): Promise<ICable>;
    createCable(requestBody: CreateCableRequest): Promise<ICable>;
    updateCable(cableId: string, requestBody: UpdateCableRequest): Promise<ICable>;
    deleteCable(cableId: string): Promise<{
        message: string;
    }>;
}
export {};
//# sourceMappingURL=cableController.d.ts.map