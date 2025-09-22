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
exports.UserController = void 0;
const tsoa_1 = require("tsoa");
const user_1 = require("../models/user");
let UserController = class UserController extends tsoa_1.Controller {
    /**
     * Get all users (Admin only)
     */
    async getUsers(page = 1, limit = 10) {
        const skip = (page - 1) * limit;
        const users = await user_1.User.find()
            .select('-password')
            .skip(skip)
            .limit(limit)
            .sort({ createdAt: -1 });
        const total = await user_1.User.countDocuments();
        return {
            users,
            total,
            pages: Math.ceil(total / limit)
        };
    }
    /**
     * Get current user profile
     */
    async getProfile(req) {
        if (!req.user) {
            throw new Error('User not found');
        }
        const user = await user_1.User.findById(req.user._id).select('-password');
        if (!user) {
            throw new Error('User not found');
        }
        return user;
    }
    /**
     * Get user by ID
     */
    async getUser(userId) {
        const user = await user_1.User.findById(userId).select('-password');
        if (!user) {
            throw new Error('User not found');
        }
        return user;
    }
    /**
     * Create a new user (Admin only)
     */
    async createUser(requestBody) {
        try {
            // Check if user already exists
            const existingUser = await user_1.User.findOne({ email: requestBody.email });
            if (existingUser) {
                throw new Error('User already exists with this email');
            }
            const user = new user_1.User(requestBody);
            user.createdAt = new Date();
            user.updatedAt = new Date();
            await user.save();
            // Return user without password
            const savedUser = await user_1.User.findById(user._id).select('-password');
            if (!savedUser) {
                throw new Error('Failed to create user');
            }
            this.setStatus(201);
            return savedUser;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    /**
     * Update user (Admin only)
     */
    async updateUser(userId, requestBody) {
        try {
            const user = await user_1.User.findByIdAndUpdate(userId, { ...requestBody }, { new: true, runValidators: true }).select('-password');
            if (!user) {
                throw new Error('User not found');
            }
            return user;
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
    /**
     * Delete user (Admin only)
     */
    async deleteUser(userId) {
        const user = await user_1.User.findByIdAndDelete(userId);
        if (!user) {
            throw new Error('User not found');
        }
        return { message: 'User deleted successfully' };
    }
};
exports.UserController = UserController;
__decorate([
    (0, tsoa_1.Get)(),
    __param(0, (0, tsoa_1.Query)()),
    __param(1, (0, tsoa_1.Query)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number, Number]),
    __metadata("design:returntype", Promise)
], UserController.prototype, "getUsers", null);
__decorate([
    (0, tsoa_1.Get)('profile'),
    __param(0, (0, tsoa_1.Request)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], UserController.prototype, "getProfile", null);
__decorate([
    (0, tsoa_1.Get)('{userId}'),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], UserController.prototype, "getUser", null);
__decorate([
    (0, tsoa_1.Post)(),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], UserController.prototype, "createUser", null);
__decorate([
    (0, tsoa_1.Put)('{userId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __param(1, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String, Object]),
    __metadata("design:returntype", Promise)
], UserController.prototype, "updateUser", null);
__decorate([
    (0, tsoa_1.Delete)('{userId}'),
    (0, tsoa_1.Security)('jwt', ['admin']),
    __param(0, (0, tsoa_1.Path)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], UserController.prototype, "deleteUser", null);
exports.UserController = UserController = __decorate([
    (0, tsoa_1.Route)('users'),
    (0, tsoa_1.Tags)('Users'),
    (0, tsoa_1.Security)('jwt')
], UserController);
//# sourceMappingURL=userController.js.map