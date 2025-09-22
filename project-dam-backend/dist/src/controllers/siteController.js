"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.SiteController = void 0;
// siteController.ts
const tsoa_1 = require("tsoa");
const site_1 = require("../models/site");
let SiteController = class SiteController extends tsoa_1.Controller {
    async getSites(page = 1, limit = 10) {
        const skip = (page - 1) * limit;
        const sites = await site_1.Site.find()
            .skip(skip)
            .limit(limit)
            .sort({ createdAt: -1 })
            .populate('devicesAtSite');
        const total = await site_1.Site.countDocuments();
        return {
            sites,
            total,
            pages: Math.ceil(total / limit)
        };
    }
    async getSite(siteId) {
        const site = await site_1.Site.findById(siteId).populate('devicesAtSite');
        if (!site) {
            throw new Error('Site not found');
        }
        return site;
    }
    async createSite(requestBody) {
        try {
            const site = new site_1.Site({
                ...requestBody,
                createdAt: new Date(),
                updatedAt: new Date()
            });
            await site.save();
            const populatedSite = await site_1.Site.findById(site._id).populate('devicesAtSite');
            if (!populatedSite) {
                throw new Error('Failed to create site');
            }
            this.setStatus(201);
            return populatedSite;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    async updateSite(siteId, requestBody) {
        try {
            const site = await site_1.Site.findByIdAndUpdate(siteId, { ...requestBody, updatedAt: new Date() }, { new: true, runValidators: true }).populate('devicesAtSite');
            if (!site) {
                throw new Error('Site not found');
            }
            return site;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    async deleteSite(siteId) {
        const site = await site_1.Site.findByIdAndDelete(siteId);
        if (!site) {
            throw new Error('Site not found');
        }
        return { message: 'Site deleted successfully' };
    }
};
exports.SiteController = SiteController;
__decorate([
    (0, tsoa_1.Get)(),
    __param(0, (0, tsoa_1.Query)()),
    __param(1, (0, tsoa_1.Query)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number, Number]),
    __metadata("design:returntype", Promise)
], SiteController.prototype, "getSites", null);
__decorate([
    (0, tsoa_1.Get)('{siteId}'),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], SiteController.prototype, "getSite", null);
__decorate([
    (0, tsoa_1.Post)(),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], SiteController.prototype, "createSite", null);
__decorate([
    (0, tsoa_1.Put)('{siteId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __param(1, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String, Object]),
    __metadata("design:returntype", Promise)
], SiteController.prototype, "updateSite", null);
__decorate([
    (0, tsoa_1.Delete)('{siteId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], SiteController.prototype, "deleteSite", null);
exports.SiteController = SiteController = __decorate([
    (0, tsoa_1.Route)('sites'),
    (0, tsoa_1.Tags)('Sites'),
    (0, tsoa_1.Security)('jwt')
], SiteController);
//# sourceMappingURL=siteController.js.map