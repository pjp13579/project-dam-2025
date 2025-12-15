// siteController.ts
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
import { Site, ISite } from '../models/site';

interface GetSitesRequest {
	_id: string;
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

@Route('sites')
@Tags('Sites')
@Security('jwt')
export class SiteController extends Controller {
	@Get()
	public async getSites(
		@Query() page: number = 1,
		@Query() limit: number = 10
	): Promise<{ sites: GetSitesRequest[]; total: number; pages: number; }> {
		const skip = (page - 1) * limit;
		const sites = await Site.find({ isActive: true })
			.skip(skip)
			.limit(limit)
			.sort({ createdAt: -1 })
			//.populate('devicesAtSite')
			;

		const total = await Site.countDocuments({ isActive: true });


		const getSitesDto: GetSitesRequest[] = sites.map(site => ({
			_id: site._id.toString(),
			localName: site.localName,
			type: site.type,
			country: site.country,
			address: {
				street: site.address.street,
				city: site.address.city,
				state: site.address.state,
				zipCode: site.address.zipCode,
				latitude: site.address.latitude,
				longitude: site.address.longitude,
			},
			isActive: site.isActive,
		}));

		return {
			sites: getSitesDto,
			total,
			pages: Math.ceil(total / limit)
		};
	}

	@Get('{siteId}')
	public async getSite(@Path() siteId: string): Promise<ISite> {
		const site = await Site.findOne({ _id: siteId, isActive: true })
			.populate({
				path: 'devicesAtSite',
				select: '_id vendor category type serialNumber macAddress state site',
				populate: {
					path: 'site',
					select: '_id localname type country address latitude longitude'
				}
			});
		if (!site) {
			throw new Error('Site not found');
		}
		return site;
	}

	@Post()
	@Security('jwt', ['admin'])
	public async createSite(@Body() requestBody: CreateSiteRequest): Promise<ISite> {
		try {
			const site = new Site({
				...requestBody,
				createdAt: new Date(),
				updatedAt: new Date()
			});
			await site.save();

			const populatedSite = await Site.findById(site._id).populate('devicesAtSite');
			if (!populatedSite) {
				throw new Error('Failed to create site');
			}

			this.setStatus(201);
			return populatedSite;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	@Put('{siteId}')
	@Security('jwt', ['admin'])
	public async updateSite(
		@Path() siteId: string,
		@Body() requestBody: UpdateSiteRequest
	): Promise<ISite> {
		try {
			const site = await Site.findByIdAndUpdate(
				{ _id: siteId, isActive: true },
				{ ...requestBody, updatedAt: new Date() },
				{ new: true, runValidators: true }
			).populate('devicesAtSite');

			if (!site) {
				throw new Error('Site not found');
			}
			return site;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	@Delete('{siteId}')
	@Security('jwt', ['admin'])
	public async deleteSite(@Path() siteId: string): Promise<{ message: string; }> {
		const site = await Site.findByIdAndUpdate(
			{ _id: siteId, isActive: true },
			{ isActive: false },
			{ new: true, runValidators: true }
		);
		// const site = await Site.findBy
		if (!site) {
			throw new Error('Site not found');
		}
		return { message: 'Site deleted successfully' };
	}
}