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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AuthController = void 0;
const tsoa_1 = require("tsoa");
const user_1 = require("../models/user");
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
let AuthController = class AuthController extends tsoa_1.Controller {
    /**
     * obtain JWT token
     */
    async login(requestBody) {
        const { email, password } = requestBody;
        const user = await user_1.User.findOne({ email, isActive: true });
        if (!user) {
            throw new Error('Invalid credentials');
        }
        console.log(user);
        const isMatch = await user.comparePassword(password);
        if (!isMatch) {
            console.log("Password does not match");
            throw new Error('Invalid credentials');
        }
        const token = jsonwebtoken_1.default.sign({ userId: user.id }, process.env.JWT_SECRET, { expiresIn: "24h" });
        return {
            token,
            user: {
                name: user.name,
                role: user.role
            }
        };
    }
    /**
     * register users
     */
    async register(requestBody) {
        try {
            const existingUser = await user_1.User.findOne({ email: requestBody.email });
            if (existingUser) {
                throw new Error('User already exists with this email');
            }
            const user = new user_1.User(requestBody);
            await user.save();
            this.setStatus(201);
            return {
                message: 'User created successfully',
                userId: user.id
            };
        }
        catch (error) {
            throw new Error(error.message);
        }
    }
};
exports.AuthController = AuthController;
__decorate([
    (0, tsoa_1.Post)('login'),
    (0, tsoa_1.SuccessResponse)('200', 'Successfully logged in'),
    __param(0, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], AuthController.prototype, "login", null);
__decorate([
    (0, tsoa_1.Post)('register'),
    (0, tsoa_1.SuccessResponse)('201', 'User created successfully'),
    __param(0, (0, tsoa_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], AuthController.prototype, "register", null);
exports.AuthController = AuthController = __decorate([
    (0, tsoa_1.Route)('auth'),
    (0, tsoa_1.Tags)('Authentication')
], AuthController);
//# sourceMappingURL=authController.js.map