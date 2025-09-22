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
exports.CableController = void 0;
const tsoa_1 = require("tsoa");
const cable_1 = require("../models/cable");
let CableController = class CableController extends tsoa_1.Controller {
    async getCables(page = 1, limit = 10) {
        const skip = (page - 1) * limit;
        const cables = await cable_1.Cable.find()
            .skip(skip)
            .limit(limit)
            .sort({ createdAt: -1 })
            .populate('device1')
            .populate('device2')
            .populate('interface1.device')
            .populate('interface2.device');
        const total = await cable_1.Cable.countDocuments();
        return {
            cables,
            total,
            pages: Math.ceil(total / limit)
        };
    }
    async getCable(cableId) {
        const cable = await cable_1.Cable.findById(cableId)
            .populate('device1')
            .populate('device2')
            .populate('interface1.device')
            .populate('interface2.device');
        if (!cable) {
            throw new Error('Cable not found');
        }
        return cable;
    }
    async createCable(requestBody) {
        try {
            const cable = new cable_1.Cable(requestBody);
            await cable.save();
            const populatedCable = await cable_1.Cable.findById(cable._id)
                .populate('device1')
                .populate('device2')
                .populate('interface1.device')
                .populate('interface2.device');
            if (!populatedCable) {
                throw new Error('Failed to create cable');
            }
            this.setStatus(201);
            return populatedCable;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    async updateCable(cableId, requestBody) {
        try {
            const cable = await cable_1.Cable.findByIdAndUpdate(cableId, requestBody, { new: true, runValidators: true })
                .populate('device1')
                .populate('device2')
                .populate('interface1.device')
                .populate('interface2.device');
            if (!cable) {
                throw new Error('Cable not found');
            }
            return cable;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    async deleteCable(cableId) {
        const cable = await cable_1.Cable.findByIdAndDelete(cableId);
        if (!cable) {
            throw new Error('Cable not found');
        }
        return { message: 'Cable deleted successfully' };
    }
};
exports.CableController = CableController;
__decorate([
    (0, tsoa_1.Get)(),
    __param(0, (0, tsoa_1.Query)()),
    __param(1, (0, tsoa_1.Query)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number, Number]),
    __metadata("design:returntype", Promise)
], CableController.prototype, "getCables", null);
__decorate([
    (0, tsoa_1.Get)('{cableId}'),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], CableController.prototype, "getCable", null);
__decorate([
    (0, tsoa_1.Post)(),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], CableController.prototype, "createCable", null);
__decorate([
    (0, tsoa_1.Put)('{cableId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __param(1, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String, Object]),
    __metadata("design:returntype", Promise)
], CableController.prototype, "updateCable", null);
__decorate([
    (0, tsoa_1.Delete)('{cableId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], CableController.prototype, "deleteCable", null);
exports.CableController = CableController = __decorate([
    (0, tsoa_1.Route)('cables'),
    (0, tsoa_1.Tags)('Cables'),
    (0, tsoa_1.Security)('jwt')
], CableController);
//# sourceMappingURL=cableController.js.map