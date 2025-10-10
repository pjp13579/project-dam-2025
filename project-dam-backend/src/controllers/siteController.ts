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
	): Promise<{ sites: ISite[]; total: number; pages: number; }> {
		const skip = (page - 1) * limit;
		const sites = await Site.find()
			.skip(skip)
			.limit(limit)
			.sort({ createdAt: -1 })
			.populate('devicesAtSite');

		const total = await Site.countDocuments();

		return {
			sites,
			total,
			pages: Math.ceil(total / limit)
		};
	}

	@Get('{siteId}')
	public async getSite(@Path() siteId: string): Promise<ISite> {
		const site = await Site.findById(siteId).populate('devicesAtSite');
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
				siteId,
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
			siteId,
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