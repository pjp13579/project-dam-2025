"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.RegisterRoutes = RegisterRoutes;
const runtime_1 = require("@tsoa/runtime");
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
const userController_1 = require("./../src/controllers/userController");
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
const siteController_1 = require("./../src/controllers/siteController");
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
const ipController_1 = require("./../src/controllers/ipController");
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
const deviceController_1 = require("./../src/controllers/deviceController");
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
const cableController_1 = require("./../src/controllers/cableController");
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
const authController_1 = require("./../src/controllers/authController");
const auth_1 = require("./../src/middleware/auth");
const expressAuthenticationRecasted = auth_1.expressAuthentication;
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
const models = {
    "mongoose.Types.ObjectId": {
        "dataType": "refAlias",
        "type": { "dataType": "string", "validators": {} },
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "IUser": {
        "dataType": "refObject",
        "properties": {
            "_id": { "ref": "mongoose.Types.ObjectId", "required": true },
            "name": { "dataType": "string", "required": true },
            "email": { "dataType": "string", "required": true },
            "password": { "dataType": "string", "required": true },
            "role": { "dataType": "union", "subSchemas": [{ "dataType": "enum", "enums": ["guest"] }, { "dataType": "enum", "enums": ["technician"] }, { "dataType": "enum", "enums": ["admin"] }], "required": true },
            "isActive": { "dataType": "boolean", "required": true },
            "createdAt": { "dataType": "datetime", "required": true },
            "updatedAt": { "dataType": "datetime", "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateUserRequest": {
        "dataType": "refObject",
        "properties": {
            "name": { "dataType": "string", "required": true },
            "email": { "dataType": "string", "required": true },
            "password": { "dataType": "string", "required": true },
            "role": { "dataType": "union", "subSchemas": [{ "dataType": "enum", "enums": ["guest"] }, { "dataType": "enum", "enums": ["technician"] }, { "dataType": "enum", "enums": ["admin"] }], "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateUserRequest": {
        "dataType": "refObject",
        "properties": {
            "name": { "dataType": "string" },
            "email": { "dataType": "string" },
            "role": { "dataType": "union", "subSchemas": [{ "dataType": "enum", "enums": ["guest"] }, { "dataType": "enum", "enums": ["technician"] }, { "dataType": "enum", "enums": ["admin"] }] },
            "isActive": { "dataType": "boolean" },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "IAddress": {
        "dataType": "refObject",
        "properties": {
            "street": { "dataType": "string", "required": true },
            "city": { "dataType": "string", "required": true },
            "state": { "dataType": "string", "required": true },
            "zipCode": { "dataType": "string", "required": true },
            "latitude": { "dataType": "double", "required": true },
            "longitude": { "dataType": "double", "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "ISite": {
        "dataType": "refObject",
        "properties": {
            "_id": { "ref": "mongoose.Types.ObjectId", "required": true },
            "localName": { "dataType": "string", "required": true },
            "type": { "dataType": "string", "required": true },
            "country": { "dataType": "string", "required": true },
            "address": { "ref": "IAddress", "required": true },
            "isActive": { "dataType": "boolean", "required": true },
            "createdAt": { "dataType": "datetime", "required": true },
            "updatedAt": { "dataType": "datetime", "required": true },
            "devicesAtSite": { "dataType": "array", "array": { "dataType": "refAlias", "ref": "mongoose.Types.ObjectId" }, "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateSiteRequest": {
        "dataType": "refObject",
        "properties": {
            "localName": { "dataType": "string", "required": true },
            "type": { "dataType": "string", "required": true },
            "country": { "dataType": "string", "required": true },
            "address": { "dataType": "nestedObjectLiteral", "nestedProperties": { "longitude": { "dataType": "double", "required": true }, "latitude": { "dataType": "double", "required": true }, "zipCode": { "dataType": "string", "required": true }, "state": { "dataType": "string", "required": true }, "city": { "dataType": "string", "required": true }, "street": { "dataType": "string", "required": true } }, "required": true },
            "isActive": { "dataType": "boolean" },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateSiteRequest": {
        "dataType": "refObject",
        "properties": {
            "localName": { "dataType": "string" },
            "type": { "dataType": "string" },
            "country": { "dataType": "string" },
            "address": { "dataType": "nestedObjectLiteral", "nestedProperties": { "longitude": { "dataType": "double" }, "latitude": { "dataType": "double" }, "zipCode": { "dataType": "string" }, "state": { "dataType": "string" }, "city": { "dataType": "string" }, "street": { "dataType": "string" } } },
            "isActive": { "dataType": "boolean" },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "IIp": {
        "dataType": "refObject",
        "properties": {
            "_id": { "ref": "mongoose.Types.ObjectId", "required": true },
            "ip": { "dataType": "string", "required": true },
            "mask": { "dataType": "double", "required": true },
            "type": { "dataType": "string", "required": true },
            "logicalEntity": { "dataType": "string", "required": true },
            "createdAt": { "dataType": "datetime", "required": true },
            "updatedAt": { "dataType": "datetime", "required": true },
            "isActive": { "dataType": "nestedObjectLiteral", "nestedProperties": { "default": { "dataType": "enum", "enums": [true], "required": true }, "type": { "dataType": "boolean", "required": true } }, "required": true },
            "devices": { "dataType": "array", "array": { "dataType": "refAlias", "ref": "mongoose.Types.ObjectId" }, "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateIpRequest": {
        "dataType": "refObject",
        "properties": {
            "ip": { "dataType": "string", "required": true },
            "mask": { "dataType": "double", "required": true },
            "type": { "dataType": "string", "required": true },
            "logicalEntity": { "dataType": "string", "required": true },
            "isActive": { "dataType": "boolean" },
            "devices": { "dataType": "array", "array": { "dataType": "string" } },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateIpRequest": {
        "dataType": "refObject",
        "properties": {
            "ip": { "dataType": "string" },
            "mask": { "dataType": "double" },
            "type": { "dataType": "string" },
            "logicalEntity": { "dataType": "string" },
            "isActive": { "dataType": "boolean" },
            "devices": { "dataType": "array", "array": { "dataType": "string" } },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "IDevice": {
        "dataType": "refObject",
        "properties": {
            "vendor": { "dataType": "string", "required": true },
            "category": { "dataType": "string", "required": true },
            "type": { "dataType": "string", "required": true },
            "serialNumber": { "dataType": "string", "required": true },
            "macAddress": { "dataType": "string", "required": true },
            "state": { "dataType": "string", "required": true },
            "site": { "ref": "mongoose.Types.ObjectId", "required": true },
            "connectedDevices": { "dataType": "array", "array": { "dataType": "refAlias", "ref": "mongoose.Types.ObjectId" }, "required": true },
            "isActive": { "dataType": "boolean", "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateDeviceRequest": {
        "dataType": "refObject",
        "properties": {
            "vendor": { "dataType": "string", "required": true },
            "category": { "dataType": "string", "required": true },
            "type": { "dataType": "string", "required": true },
            "serialNumber": { "dataType": "string", "required": true },
            "macAddress": { "dataType": "string", "required": true },
            "state": { "dataType": "string", "required": true },
            "site": { "dataType": "string", "required": true },
            "connectedDevices": { "dataType": "array", "array": { "dataType": "string" } },
            "isActive": { "dataType": "boolean" },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateDeviceRequest": {
        "dataType": "refObject",
        "properties": {
            "vendor": { "dataType": "string" },
            "category": { "dataType": "string" },
            "type": { "dataType": "string" },
            "serialNumber": { "dataType": "string" },
            "macAddress": { "dataType": "string" },
            "state": { "dataType": "string" },
            "site": { "dataType": "string" },
            "connectedDevices": { "dataType": "array", "array": { "dataType": "string" } },
            "isActive": { "dataType": "boolean" },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "ICable": {
        "dataType": "refObject",
        "properties": {
            "device1": { "ref": "mongoose.Types.ObjectId", "required": true },
            "device2": { "ref": "mongoose.Types.ObjectId", "required": true },
            "cableType": { "dataType": "string", "required": true },
            "interface1": { "dataType": "nestedObjectLiteral", "nestedProperties": { "duplex": { "dataType": "string", "required": true }, "linkSpeed": { "dataType": "string", "required": true }, "portId": { "dataType": "string", "required": true }, "device": { "ref": "mongoose.Types.ObjectId", "required": true } }, "required": true },
            "interface2": { "dataType": "nestedObjectLiteral", "nestedProperties": { "duplex": { "dataType": "string", "required": true }, "linkSpeed": { "dataType": "string", "required": true }, "portId": { "dataType": "string", "required": true }, "device": { "ref": "mongoose.Types.ObjectId", "required": true } }, "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateCableRequest": {
        "dataType": "refObject",
        "properties": {
            "device1": { "dataType": "string", "required": true },
            "device2": { "dataType": "string", "required": true },
            "cableType": { "dataType": "string", "required": true },
            "interface1": { "dataType": "nestedObjectLiteral", "nestedProperties": { "duplex": { "dataType": "string", "required": true }, "linkSpeed": { "dataType": "string", "required": true }, "portId": { "dataType": "string", "required": true }, "device": { "dataType": "string", "required": true } }, "required": true },
            "interface2": { "dataType": "nestedObjectLiteral", "nestedProperties": { "duplex": { "dataType": "string", "required": true }, "linkSpeed": { "dataType": "string", "required": true }, "portId": { "dataType": "string", "required": true }, "device": { "dataType": "string", "required": true } }, "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateCableRequest": {
        "dataType": "refObject",
        "properties": {
            "device1": { "dataType": "string" },
            "device2": { "dataType": "string" },
            "cableType": { "dataType": "string" },
            "interface1": { "dataType": "nestedObjectLiteral", "nestedProperties": { "duplex": { "dataType": "string" }, "linkSpeed": { "dataType": "string" }, "portId": { "dataType": "string" }, "device": { "dataType": "string" } } },
            "interface2": { "dataType": "nestedObjectLiteral", "nestedProperties": { "duplex": { "dataType": "string" }, "linkSpeed": { "dataType": "string" }, "portId": { "dataType": "string" }, "device": { "dataType": "string" } } },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "LoginResponse": {
        "dataType": "refObject",
        "properties": {
            "token": { "dataType": "string", "required": true },
            "user": { "dataType": "nestedObjectLiteral", "nestedProperties": { "role": { "dataType": "string", "required": true }, "email": { "dataType": "string", "required": true }, "name": { "dataType": "string", "required": true }, "id": { "dataType": "string", "required": true } }, "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "LoginRequest": {
        "dataType": "refObject",
        "properties": {
            "email": { "dataType": "string", "required": true },
            "password": { "dataType": "string", "required": true },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "RegisterRequest": {
        "dataType": "refObject",
        "properties": {
            "name": { "dataType": "string", "required": true },
            "email": { "dataType": "string", "required": true },
            "password": { "dataType": "string", "required": true },
            "role": { "dataType": "union", "subSchemas": [{ "dataType": "enum", "enums": ["guest"] }, { "dataType": "enum", "enums": ["technician"] }, { "dataType": "enum", "enums": ["admin"] }] },
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
};
const templateService = new runtime_1.ExpressTemplateService(models, { "noImplicitAdditionalProperties": "throw-on-extras", "bodyCoercion": true });
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
function RegisterRoutes(app) {
    // ###########################################################################################################
    //  NOTE: If you do not see routes for all of your controllers in this file, then you might not have informed tsoa of where to look
    //      Please look into the "controllerPathGlobs" config option described in the readme: https://github.com/lukeautry/tsoa
    // ###########################################################################################################
    const argsUserController_getUsers = {
        page: { "default": 1, "in": "query", "name": "page", "dataType": "double" },
        limit: { "default": 10, "in": "query", "name": "limit", "dataType": "double" },
    };
    app.get('/users', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController)), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController.prototype.getUsers)), async function UserController_getUsers(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsUserController_getUsers, request, response });
            const controller = new userController_1.UserController();
            await templateService.apiHandler({
                methodName: 'getUsers',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsUserController_getProfile = {
        req: { "in": "request", "name": "req", "required": true, "dataType": "object" },
    };
    app.get('/users/profile', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController)), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController.prototype.getProfile)), async function UserController_getProfile(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsUserController_getProfile, request, response });
            const controller = new userController_1.UserController();
            await templateService.apiHandler({
                methodName: 'getProfile',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsUserController_getUser = {
        userId: { "in": "path", "name": "userId", "required": true, "dataType": "string" },
    };
    app.get('/users/:userId', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController)), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController.prototype.getUser)), async function UserController_getUser(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsUserController_getUser, request, response });
            const controller = new userController_1.UserController();
            await templateService.apiHandler({
                methodName: 'getUser',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsUserController_createUser = {
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "CreateUserRequest" },
    };
    app.post('/users', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController)), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController.prototype.createUser)), async function UserController_createUser(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsUserController_createUser, request, response });
            const controller = new userController_1.UserController();
            await templateService.apiHandler({
                methodName: 'createUser',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsUserController_updateUser = {
        userId: { "in": "path", "name": "userId", "required": true, "dataType": "string" },
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "UpdateUserRequest" },
    };
    app.put('/users/:userId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController)), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController.prototype.updateUser)), async function UserController_updateUser(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsUserController_updateUser, request, response });
            const controller = new userController_1.UserController();
            await templateService.apiHandler({
                methodName: 'updateUser',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsUserController_deleteUser = {
        userId: { "in": "path", "name": "userId", "required": true, "dataType": "string" },
    };
    app.delete('/users/:userId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController)), ...((0, runtime_1.fetchMiddlewares)(userController_1.UserController.prototype.deleteUser)), async function UserController_deleteUser(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsUserController_deleteUser, request, response });
            const controller = new userController_1.UserController();
            await templateService.apiHandler({
                methodName: 'deleteUser',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsSiteController_getSites = {
        page: { "default": 1, "in": "query", "name": "page", "dataType": "double" },
        limit: { "default": 10, "in": "query", "name": "limit", "dataType": "double" },
    };
    app.get('/sites', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController)), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController.prototype.getSites)), async function SiteController_getSites(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_getSites, request, response });
            const controller = new siteController_1.SiteController();
            await templateService.apiHandler({
                methodName: 'getSites',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsSiteController_getSite = {
        siteId: { "in": "path", "name": "siteId", "required": true, "dataType": "string" },
    };
    app.get('/sites/:siteId', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController)), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController.prototype.getSite)), async function SiteController_getSite(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_getSite, request, response });
            const controller = new siteController_1.SiteController();
            await templateService.apiHandler({
                methodName: 'getSite',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsSiteController_createSite = {
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "CreateSiteRequest" },
    };
    app.post('/sites', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController)), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController.prototype.createSite)), async function SiteController_createSite(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_createSite, request, response });
            const controller = new siteController_1.SiteController();
            await templateService.apiHandler({
                methodName: 'createSite',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsSiteController_updateSite = {
        siteId: { "in": "path", "name": "siteId", "required": true, "dataType": "string" },
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "UpdateSiteRequest" },
    };
    app.put('/sites/:siteId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController)), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController.prototype.updateSite)), async function SiteController_updateSite(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_updateSite, request, response });
            const controller = new siteController_1.SiteController();
            await templateService.apiHandler({
                methodName: 'updateSite',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsSiteController_deleteSite = {
        siteId: { "in": "path", "name": "siteId", "required": true, "dataType": "string" },
    };
    app.delete('/sites/:siteId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController)), ...((0, runtime_1.fetchMiddlewares)(siteController_1.SiteController.prototype.deleteSite)), async function SiteController_deleteSite(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_deleteSite, request, response });
            const controller = new siteController_1.SiteController();
            await templateService.apiHandler({
                methodName: 'deleteSite',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsIpController_getIps = {
        page: { "default": 1, "in": "query", "name": "page", "dataType": "double" },
        limit: { "default": 10, "in": "query", "name": "limit", "dataType": "double" },
    };
    app.get('/ips', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController)), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController.prototype.getIps)), async function IpController_getIps(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsIpController_getIps, request, response });
            const controller = new ipController_1.IpController();
            await templateService.apiHandler({
                methodName: 'getIps',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsIpController_getIp = {
        ipId: { "in": "path", "name": "ipId", "required": true, "dataType": "string" },
    };
    app.get('/ips/:ipId', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController)), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController.prototype.getIp)), async function IpController_getIp(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsIpController_getIp, request, response });
            const controller = new ipController_1.IpController();
            await templateService.apiHandler({
                methodName: 'getIp',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsIpController_createIp = {
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "CreateIpRequest" },
    };
    app.post('/ips', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController)), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController.prototype.createIp)), async function IpController_createIp(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsIpController_createIp, request, response });
            const controller = new ipController_1.IpController();
            await templateService.apiHandler({
                methodName: 'createIp',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsIpController_updateIp = {
        ipId: { "in": "path", "name": "ipId", "required": true, "dataType": "string" },
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "UpdateIpRequest" },
    };
    app.put('/ips/:ipId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController)), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController.prototype.updateIp)), async function IpController_updateIp(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsIpController_updateIp, request, response });
            const controller = new ipController_1.IpController();
            await templateService.apiHandler({
                methodName: 'updateIp',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsIpController_deleteIp = {
        ipId: { "in": "path", "name": "ipId", "required": true, "dataType": "string" },
    };
    app.delete('/ips/:ipId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController)), ...((0, runtime_1.fetchMiddlewares)(ipController_1.IpController.prototype.deleteIp)), async function IpController_deleteIp(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsIpController_deleteIp, request, response });
            const controller = new ipController_1.IpController();
            await templateService.apiHandler({
                methodName: 'deleteIp',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsDeviceController_getDevices = {
        page: { "default": 1, "in": "query", "name": "page", "dataType": "double" },
        limit: { "default": 10, "in": "query", "name": "limit", "dataType": "double" },
    };
    app.get('/devices', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController)), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController.prototype.getDevices)), async function DeviceController_getDevices(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_getDevices, request, response });
            const controller = new deviceController_1.DeviceController();
            await templateService.apiHandler({
                methodName: 'getDevices',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsDeviceController_getDevice = {
        deviceId: { "in": "path", "name": "deviceId", "required": true, "dataType": "string" },
    };
    app.get('/devices/:deviceId', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController)), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController.prototype.getDevice)), async function DeviceController_getDevice(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_getDevice, request, response });
            const controller = new deviceController_1.DeviceController();
            await templateService.apiHandler({
                methodName: 'getDevice',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsDeviceController_createDevice = {
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "CreateDeviceRequest" },
    };
    app.post('/devices', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController)), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController.prototype.createDevice)), async function DeviceController_createDevice(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_createDevice, request, response });
            const controller = new deviceController_1.DeviceController();
            await templateService.apiHandler({
                methodName: 'createDevice',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsDeviceController_updateDevice = {
        deviceId: { "in": "path", "name": "deviceId", "required": true, "dataType": "string" },
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "UpdateDeviceRequest" },
    };
    app.put('/devices/:deviceId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController)), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController.prototype.updateDevice)), async function DeviceController_updateDevice(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_updateDevice, request, response });
            const controller = new deviceController_1.DeviceController();
            await templateService.apiHandler({
                methodName: 'updateDevice',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsDeviceController_deleteDevice = {
        deviceId: { "in": "path", "name": "deviceId", "required": true, "dataType": "string" },
    };
    app.delete('/devices/:deviceId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController)), ...((0, runtime_1.fetchMiddlewares)(deviceController_1.DeviceController.prototype.deleteDevice)), async function DeviceController_deleteDevice(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_deleteDevice, request, response });
            const controller = new deviceController_1.DeviceController();
            await templateService.apiHandler({
                methodName: 'deleteDevice',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsCableController_getCables = {
        page: { "default": 1, "in": "query", "name": "page", "dataType": "double" },
        limit: { "default": 10, "in": "query", "name": "limit", "dataType": "double" },
    };
    app.get('/cables', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController)), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController.prototype.getCables)), async function CableController_getCables(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsCableController_getCables, request, response });
            const controller = new cableController_1.CableController();
            await templateService.apiHandler({
                methodName: 'getCables',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsCableController_getCable = {
        cableId: { "in": "path", "name": "cableId", "required": true, "dataType": "string" },
    };
    app.get('/cables/:cableId', authenticateMiddleware([{ "jwt": [] }]), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController)), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController.prototype.getCable)), async function CableController_getCable(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsCableController_getCable, request, response });
            const controller = new cableController_1.CableController();
            await templateService.apiHandler({
                methodName: 'getCable',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsCableController_createCable = {
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "CreateCableRequest" },
    };
    app.post('/cables', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController)), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController.prototype.createCable)), async function CableController_createCable(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsCableController_createCable, request, response });
            const controller = new cableController_1.CableController();
            await templateService.apiHandler({
                methodName: 'createCable',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsCableController_updateCable = {
        cableId: { "in": "path", "name": "cableId", "required": true, "dataType": "string" },
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "UpdateCableRequest" },
    };
    app.put('/cables/:cableId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController)), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController.prototype.updateCable)), async function CableController_updateCable(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsCableController_updateCable, request, response });
            const controller = new cableController_1.CableController();
            await templateService.apiHandler({
                methodName: 'updateCable',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsCableController_deleteCable = {
        cableId: { "in": "path", "name": "cableId", "required": true, "dataType": "string" },
    };
    app.delete('/cables/:cableId', authenticateMiddleware([{ "jwt": ["admin"] }]), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController)), ...((0, runtime_1.fetchMiddlewares)(cableController_1.CableController.prototype.deleteCable)), async function CableController_deleteCable(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsCableController_deleteCable, request, response });
            const controller = new cableController_1.CableController();
            await templateService.apiHandler({
                methodName: 'deleteCable',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsAuthController_login = {
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "LoginRequest" },
    };
    app.post('/auth/login', ...((0, runtime_1.fetchMiddlewares)(authController_1.AuthController)), ...((0, runtime_1.fetchMiddlewares)(authController_1.AuthController.prototype.login)), async function AuthController_login(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsAuthController_login, request, response });
            const controller = new authController_1.AuthController();
            await templateService.apiHandler({
                methodName: 'login',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: 200,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    const argsAuthController_register = {
        requestBody: { "in": "body", "name": "requestBody", "required": true, "ref": "RegisterRequest" },
    };
    app.post('/auth/register', ...((0, runtime_1.fetchMiddlewares)(authController_1.AuthController)), ...((0, runtime_1.fetchMiddlewares)(authController_1.AuthController.prototype.register)), async function AuthController_register(request, response, next) {
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        let validatedArgs = [];
        try {
            validatedArgs = templateService.getValidatedArgs({ args: argsAuthController_register, request, response });
            const controller = new authController_1.AuthController();
            await templateService.apiHandler({
                methodName: 'register',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: 201,
            });
        }
        catch (err) {
            return next(err);
        }
    });
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    function authenticateMiddleware(security = []) {
        return async function runAuthenticationMiddleware(request, response, next) {
            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
            // keep track of failed auth attempts so we can hand back the most
            // recent one.  This behavior was previously existing so preserving it
            // here
            const failedAttempts = [];
            const pushAndRethrow = (error) => {
                failedAttempts.push(error);
                throw error;
            };
            const secMethodOrPromises = [];
            for (const secMethod of security) {
                if (Object.keys(secMethod).length > 1) {
                    const secMethodAndPromises = [];
                    for (const name in secMethod) {
                        secMethodAndPromises.push(expressAuthenticationRecasted(request, name, secMethod[name], response)
                            .catch(pushAndRethrow));
                    }
                    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
                    secMethodOrPromises.push(Promise.all(secMethodAndPromises)
                        .then(users => { return users[0]; }));
                }
                else {
                    for (const name in secMethod) {
                        secMethodOrPromises.push(expressAuthenticationRecasted(request, name, secMethod[name], response)
                            .catch(pushAndRethrow));
                    }
                }
            }
            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
            try {
                request['user'] = await Promise.any(secMethodOrPromises);
                // Response was sent in middleware, abort
                if (response.writableEnded) {
                    return;
                }
                next();
            }
            catch (err) {
                // Show most recent error as response
                const error = failedAttempts.pop();
                error.status = error.status || 401;
                // Response was sent in middleware, abort
                if (response.writableEnded) {
                    return;
                }
                next(error);
            }
            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        };
    }
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
}
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
//# sourceMappingURL=routes.js.map