"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const mongoose_1 = __importDefault(require("mongoose"));
const cors_1 = __importDefault(require("cors"));
const dotenv_1 = __importDefault(require("dotenv"));
const routes_1 = require("./dist/routes");
const swagger_ui_express_1 = __importDefault(require("swagger-ui-express"));
const errorHandler_1 = require("./src/middleware/errorHandler");
const auth_1 = require("./src/middleware/auth");
// load .env
dotenv_1.default.config();
// create and config express app
const app = (0, express_1.default)();
const PORT = process.env.PORT || 5000;
// middleware
app.use((0, cors_1.default)());
app.use(express_1.default.json());
// serve swagger ui
app.use('/docs', swagger_ui_express_1.default.serve, async (_req, res) => {
    return res.send(swagger_ui_express_1.default.generateHTML(await Promise.resolve().then(() => __importStar(require('./dist/swagger.json'))))); // "resolveJsonModule": true
});
// apply auth middleware to all routes except auth and docs
app.use((req, res, next) => {
    if (req.path === '/auth/login' || req.path.startsWith('/docs')) {
        return next();
    }
    (0, auth_1.expressAuthentication)(req, 'jwt')
        .then(user => {
        // Optionally attach user to request
        req.user = user;
        next();
    })
        .catch(err => {
        res.status(401).json({ message: err.message });
    });
});
// register tsoa routes
(0, routes_1.RegisterRoutes)(app);
// error handling middleware
app.use(errorHandler_1.errorHandler);
console.log("Connecting to MongoDB...");
console.log("proc ess.env.MONGODB_URI: " + process.env.MONGODB_URI);
console.log("proc ess.env.MONGODB_URI as string: " + process.env.MONGODB_URI);
// mongoDB connection
mongoose_1.default.connect("mongodb+srv://rgCras:imagensbase64!@dam20251.global.mongocluster.cosmos.azure.com/dam?tls=true&authMechanism=SCRAM-SHA-256&retrywrites=false&maxIdleTimeMS=120000")
    .then(() => console.log('MongoDB connected successfully'))
    .catch(err => console.error('MongoDB connection error:', err));
// start server
app.listen(PORT, () => {
    console.log(`CacoRedes API server is running on port ${PORT}`);
    console.log(`API documentation available at http://localhost:${PORT}/docs`);
});
//# sourceMappingURL=server.js.map