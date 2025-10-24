import { Controller } from 'tsoa';
import { ISite } from '../models/site';
interface GetSitesRequest {
    localName: string;
    type: string;
    country: string;
    address: {
        street: string;
        city: string;
        state: string;
        zipCode: string;
        latitude: number;
        longitude: number;
    };
    isActive?: boolean;
}
interface CreateSiteRequest {
    localName: string;
    type: string;
    country: string;
    address: {
        street: string;
        city: string;
        state: string;
        zipCode: string;
        latitude: number;
        longitude: number;
    };
    devicesAtSite: string[];
    isActive?: boolean;
}
interface UpdateSiteRequest {
    localName?: string;
    type?: string;
    country?: string;
    address?: {
        street?: string;
        city?: string;
        state?: string;
        zipCode?: string;
        latitude?: number;
        longitude?: number;
    };
    isActive?: boolean;
}
export declare class SiteController extends Controller {
    getSites(page?: number, limit?: number): Promise<{
        sites: GetSitesRequest[];
        total: number;
        pages: number;
    }>;
    getSite(siteId: string): Promise<ISite>;
    createSite(requestBody: CreateSiteRequest): Promise<ISite>;
    updateSite(siteId: string, requestBody: UpdateSiteRequest): Promise<ISite>;
    deleteSite(siteId: string): Promise<{
        message: string;
    }>;
}
export {};
//# sourceMappingURL=siteController.d.ts.map