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
exports.IpController = void 0;
// ipController.ts
const tsoa_1 = require("tsoa");
const ip_1 = require("../models/ip");
let IpController = class IpController extends tsoa_1.Controller {
    async getIps(page = 1, limit = 10) {
        const skip = (page - 1) * limit;
        const ips = await ip_1.Ip.find()
            .skip(skip)
            .limit(limit)
            .sort({ createdAt: -1 })
            .populate('devices');
        const total = await ip_1.Ip.countDocuments();
        return {
            ips,
            total,
            pages: Math.ceil(total / limit)
        };
    }
    async getIp(ipId) {
        const ip = await ip_1.Ip.findById(ipId).populate('devices');
        if (!ip) {
            throw new Error('IP not found');
        }
        return ip;
    }
    async createIp(requestBody) {
        try {
            const ip = new ip_1.Ip({
                ...requestBody,
                createdAt: new Date(),
                updatedAt: new Date()
            });
            await ip.save();
            const populatedIp = await ip_1.Ip.findById(ip._id).populate('devices');
            if (!populatedIp) {
                throw new Error('Failed to create IP');
            }
            this.setStatus(201);
            return populatedIp;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    async updateIp(ipId, requestBody) {
        try {
            const ip = await ip_1.Ip.findByIdAndUpdate(ipId, { ...requestBody, updatedAt: new Date() }, { new: true, runValidators: true }).populate('devices');
            if (!ip) {
                throw new Error('IP not found');
            }
            return ip;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    async deleteIp(ipId) {
        const ip = await ip_1.Ip.findByIdAndDelete(ipId);
        if (!ip) {
            throw new Error('IP not found');
        }
        return { message: 'IP deleted successfully' };
    }
};
exports.IpController = IpController;
__decorate([
    (0, tsoa_1.Get)(),
    __param(0, (0, tsoa_1.Query)()),
    __param(1, (0, tsoa_1.Query)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number, Number]),
    __metadata("design:returntype", Promise)
], IpController.prototype, "getIps", null);
__decorate([
    (0, tsoa_1.Get)('{ipId}'),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], IpController.prototype, "getIp", null);
__decorate([
    (0, tsoa_1.Post)(),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], IpController.prototype, "createIp", null);
__decorate([
    (0, tsoa_1.Put)('{ipId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __param(1, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String, Object]),
    __metadata("design:returntype", Promise)
], IpController.prototype, "updateIp", null);
__decorate([
    (0, tsoa_1.Delete)('{ipId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], IpController.prototype, "deleteIp", null);
exports.IpController = IpController = __decorate([
    (0, tsoa_1.Route)('ips'),
    (0, tsoa_1.Tags)('IPs'),
    (0, tsoa_1.Security)('jwt')
], IpController);
//# sourceMappingURL=ipController.js.map