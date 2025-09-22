"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.requireRole = void 0;
exports.expressAuthentication = expressAuthentication;
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
const user_1 = require("../models/user");
// Add this to your auth.ts file
async function expressAuthentication(request, securityName, scopes) {
    if (securityName !== 'jwt') {
        throw new Error('Unsupported security name');
    }
    // Authenticate using your existing logic
    const token = request.header('Authorization')?.replace('Bearer ', '');
    if (!token) {
        throw new Error('Access denied. No token provided.');
    }
    const decoded = jsonwebtoken_1.default.verify(token, process.env.JWT_SECRET);
    const user = await user_1.User.findById(decoded.userId).select('-password');
    if (!user) {
        throw new Error('Token is not valid.');
    }
    // Check scopes if provided
    if (scopes && scopes.length > 0) {
        if (!scopes.includes(user.role)) {
            throw new Error('Access denied. Insufficient permissions.');
        }
    }
    return user;
}
const requireRole = (roles) => {
    return (req, res, next) => {
        if (!req.user || !roles.includes(req.user.role)) {
            return res.status(403).json({
                message: 'Access denied. Insufficient permissions.'
            });
        }
        next();
    };
};
exports.requireRole = requireRole;
//# sourceMappingURL=auth.js.map