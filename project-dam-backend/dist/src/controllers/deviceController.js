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
exports.DeviceController = void 0;
// deviceController.ts
const tsoa_1 = require("tsoa");
const device_1 = require("../models/device");
let DeviceController = class DeviceController extends tsoa_1.Controller {
    async getDevices(page = 1, limit = 10) {
        const skip = (page - 1) * limit;
        const devices = await device_1.Device.find()
            .skip(skip)
            .limit(limit)
            .sort({ createdAt: -1 })
            .populate('site')
            .populate('connectedDevices');
        const total = await device_1.Device.countDocuments();
        return {
            devices,
            total,
            pages: Math.ceil(total / limit)
        };
    }
    async getDevice(deviceId) {
        const device = await device_1.Device.findById(deviceId)
            .populate('site')
            .populate('connectedDevices');
        if (!device) {
            throw new Error('Device not found');
        }
        return device;
    }
    async createDevice(requestBody) {
        try {
            const device = new device_1.Device({
                ...requestBody,
                createdAt: new Date(),
                updatedAt: new Date()
            });
            await device.save();
            const populatedDevice = await device_1.Device.findById(device._id)
                .populate('site')
                .populate('connectedDevices');
            if (!populatedDevice) {
                throw new Error('Failed to create device');
            }
            this.setStatus(201);
            return populatedDevice;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    async updateDevice(deviceId, requestBody) {
        try {
            const device = await device_1.Device.findByIdAndUpdate(deviceId, { ...requestBody, updatedAt: new Date() }, { new: true, runValidators: true })
                .populate('site')
                .populate('connectedDevices');
            if (!device) {
                throw new Error('Device not found');
            }
            return device;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    async deleteDevice(deviceId) {
        const device = await device_1.Device.findByIdAndUpdate(deviceId, { isActive: false }, { new: true, runValidators: true });
        if (!device) {
            throw new Error('Device not found');
        }
        return { message: 'Device deleted successfully' };
    }
};
exports.DeviceController = DeviceController;
__decorate([
    (0, tsoa_1.Get)(),
    __param(0, (0, tsoa_1.Query)()),
    __param(1, (0, tsoa_1.Query)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number, Number]),
    __metadata("design:returntype", Promise)
], DeviceController.prototype, "getDevices", null);
__decorate([
    (0, tsoa_1.Get)('{deviceId}'),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], DeviceController.prototype, "getDevice", null);
__decorate([
    (0, tsoa_1.Post)(),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], DeviceController.prototype, "createDevice", null);
__decorate([
    (0, tsoa_1.Put)('{deviceId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __param(1, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String, Object]),
    __metadata("design:returntype", Promise)
], DeviceController.prototype, "updateDevice", null);
__decorate([
    (0, tsoa_1.Delete)('{deviceId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], DeviceController.prototype, "deleteDevice", null);
exports.DeviceController = DeviceController = __decorate([
    (0, tsoa_1.Route)('devices'),
    (0, tsoa_1.Tags)('Devices'),
    (0, tsoa_1.Security)('jwt')
], DeviceController);
//# sourceMappingURL=deviceController.js.map