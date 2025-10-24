/* tslint:disable */
/* eslint-disable */
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
import type { TsoaRoute } from '@tsoa/runtime';
import {  fetchMiddlewares, ExpressTemplateService } from '@tsoa/runtime';
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
import { UserController } from './../src/controllers/userController';
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
import { SiteController } from './../src/controllers/siteController';
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
import { IpController } from './../src/controllers/ipController';
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
import { DeviceController } from './../src/controllers/deviceController';
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
import { CableController } from './../src/controllers/cableController';
// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
import { AuthController } from './../src/controllers/authController';
import { expressAuthentication } from './../src/middleware/auth';
// @ts-ignore - no great way to install types from subpackage
import type { Request as ExRequest, Response as ExResponse, RequestHandler, Router } from 'express';

const expressAuthenticationRecasted = expressAuthentication as (req: ExRequest, securityName: string, scopes?: string[], res?: ExResponse) => Promise<any>;


// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

const models: TsoaRoute.Models = {
    "GetUser": {
        "dataType": "refObject",
        "properties": {
            "email": {"dataType":"string","required":true},
            "name": {"dataType":"string","required":true},
            "role": {"dataType":"string","required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "mongoose.Types.ObjectId": {
        "dataType": "refAlias",
        "type": {"dataType":"string","validators":{}},
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "IUser": {
        "dataType": "refObject",
        "properties": {
            "_id": {"ref":"mongoose.Types.ObjectId","required":true},
            "name": {"dataType":"string","required":true},
            "email": {"dataType":"string","required":true},
            "password": {"dataType":"string","required":true},
            "role": {"dataType":"union","subSchemas":[{"dataType":"enum","enums":["guest"]},{"dataType":"enum","enums":["technician"]},{"dataType":"enum","enums":["admin"]}],"required":true},
            "isActive": {"dataType":"boolean","required":true},
            "createdAt": {"dataType":"datetime","required":true},
            "updatedAt": {"dataType":"datetime","required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateUserRequest": {
        "dataType": "refObject",
        "properties": {
            "name": {"dataType":"string","required":true},
            "email": {"dataType":"string","required":true},
            "password": {"dataType":"string","required":true},
            "role": {"dataType":"union","subSchemas":[{"dataType":"enum","enums":["guest"]},{"dataType":"enum","enums":["technician"]},{"dataType":"enum","enums":["admin"]}],"required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateUserRequest": {
        "dataType": "refObject",
        "properties": {
            "name": {"dataType":"string"},
            "email": {"dataType":"string"},
            "role": {"dataType":"union","subSchemas":[{"dataType":"enum","enums":["guest"]},{"dataType":"enum","enums":["technician"]},{"dataType":"enum","enums":["admin"]}]},
            "isActive": {"dataType":"boolean"},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "GetSitesRequest": {
        "dataType": "refObject",
        "properties": {
            "_id": {"dataType":"string","required":true},
            "localName": {"dataType":"string","required":true},
            "type": {"dataType":"string","required":true},
            "country": {"dataType":"string","required":true},
            "address": {"dataType":"nestedObjectLiteral","nestedProperties":{"longitude":{"dataType":"double","required":true},"latitude":{"dataType":"double","required":true},"zipCode":{"dataType":"string","required":true},"state":{"dataType":"string","required":true},"city":{"dataType":"string","required":true},"street":{"dataType":"string","required":true}},"required":true},
            "isActive": {"dataType":"boolean"},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "IAddress": {
        "dataType": "refObject",
        "properties": {
            "street": {"dataType":"string","required":true},
            "city": {"dataType":"string","required":true},
            "state": {"dataType":"string","required":true},
            "zipCode": {"dataType":"string","required":true},
            "latitude": {"dataType":"double","required":true},
            "longitude": {"dataType":"double","required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "ISite": {
        "dataType": "refObject",
        "properties": {
            "_id": {"ref":"mongoose.Types.ObjectId","required":true},
            "localName": {"dataType":"string","required":true},
            "type": {"dataType":"string","required":true},
            "country": {"dataType":"string","required":true},
            "address": {"ref":"IAddress","required":true},
            "isActive": {"dataType":"boolean","required":true},
            "createdAt": {"dataType":"datetime","required":true},
            "updatedAt": {"dataType":"datetime","required":true},
            "devicesAtSite": {"dataType":"array","array":{"dataType":"refAlias","ref":"mongoose.Types.ObjectId"},"required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateSiteRequest": {
        "dataType": "refObject",
        "properties": {
            "localName": {"dataType":"string","required":true},
            "type": {"dataType":"string","required":true},
            "country": {"dataType":"string","required":true},
            "address": {"dataType":"nestedObjectLiteral","nestedProperties":{"longitude":{"dataType":"double","required":true},"latitude":{"dataType":"double","required":true},"zipCode":{"dataType":"string","required":true},"state":{"dataType":"string","required":true},"city":{"dataType":"string","required":true},"street":{"dataType":"string","required":true}},"required":true},
            "devicesAtSite": {"dataType":"array","array":{"dataType":"string"},"required":true},
            "isActive": {"dataType":"boolean"},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateSiteRequest": {
        "dataType": "refObject",
        "properties": {
            "localName": {"dataType":"string"},
            "type": {"dataType":"string"},
            "country": {"dataType":"string"},
            "address": {"dataType":"nestedObjectLiteral","nestedProperties":{"longitude":{"dataType":"double"},"latitude":{"dataType":"double"},"zipCode":{"dataType":"string"},"state":{"dataType":"string"},"city":{"dataType":"string"},"street":{"dataType":"string"}}},
            "isActive": {"dataType":"boolean"},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "IIp": {
        "dataType": "refObject",
        "properties": {
            "_id": {"ref":"mongoose.Types.ObjectId","required":true},
            "ip": {"dataType":"string","required":true},
            "mask": {"dataType":"double","required":true},
            "type": {"dataType":"string","required":true},
            "logicalEntity": {"dataType":"string","required":true},
            "createdAt": {"dataType":"datetime","required":true},
            "updatedAt": {"dataType":"datetime","required":true},
            "isActive": {"dataType":"nestedObjectLiteral","nestedProperties":{"default":{"dataType":"enum","enums":[true],"required":true},"type":{"dataType":"boolean","required":true}},"required":true},
            "devices": {"dataType":"array","array":{"dataType":"refAlias","ref":"mongoose.Types.ObjectId"},"required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateIpRequest": {
        "dataType": "refObject",
        "properties": {
            "ip": {"dataType":"string","required":true},
            "mask": {"dataType":"double","required":true},
            "type": {"dataType":"string","required":true},
            "logicalEntity": {"dataType":"string","required":true},
            "isActive": {"dataType":"boolean"},
            "devices": {"dataType":"array","array":{"dataType":"string"}},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateIpRequest": {
        "dataType": "refObject",
        "properties": {
            "ip": {"dataType":"string"},
            "mask": {"dataType":"double"},
            "type": {"dataType":"string"},
            "logicalEntity": {"dataType":"string"},
            "isActive": {"dataType":"boolean"},
            "devices": {"dataType":"array","array":{"dataType":"string"}},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "IDevice": {
        "dataType": "refObject",
        "properties": {
            "vendor": {"dataType":"string","required":true},
            "category": {"dataType":"string","required":true},
            "type": {"dataType":"string","required":true},
            "serialNumber": {"dataType":"string","required":true},
            "macAddress": {"dataType":"string","required":true},
            "state": {"dataType":"string","required":true},
            "site": {"ref":"mongoose.Types.ObjectId","required":true},
            "connectedDevices": {"dataType":"array","array":{"dataType":"refAlias","ref":"mongoose.Types.ObjectId"},"required":true},
            "isActive": {"dataType":"boolean","required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateDeviceRequest": {
        "dataType": "refObject",
        "properties": {
            "vendor": {"dataType":"string","required":true},
            "category": {"dataType":"string","required":true},
            "type": {"dataType":"string","required":true},
            "serialNumber": {"dataType":"string","required":true},
            "macAddress": {"dataType":"string","required":true},
            "state": {"dataType":"string","required":true},
            "site": {"dataType":"string","required":true},
            "connectedDevices": {"dataType":"array","array":{"dataType":"string"}},
            "isActive": {"dataType":"boolean"},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateDeviceRequest": {
        "dataType": "refObject",
        "properties": {
            "vendor": {"dataType":"string"},
            "category": {"dataType":"string"},
            "type": {"dataType":"string"},
            "serialNumber": {"dataType":"string"},
            "macAddress": {"dataType":"string"},
            "state": {"dataType":"string"},
            "site": {"dataType":"string"},
            "connectedDevices": {"dataType":"array","array":{"dataType":"string"}},
            "isActive": {"dataType":"boolean"},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "ICable": {
        "dataType": "refObject",
        "properties": {
            "device1": {"ref":"mongoose.Types.ObjectId","required":true},
            "device2": {"ref":"mongoose.Types.ObjectId","required":true},
            "cableType": {"dataType":"string","required":true},
            "interface1": {"dataType":"nestedObjectLiteral","nestedProperties":{"duplex":{"dataType":"string","required":true},"linkSpeed":{"dataType":"string","required":true},"portId":{"dataType":"string","required":true},"device":{"ref":"mongoose.Types.ObjectId","required":true}},"required":true},
            "interface2": {"dataType":"nestedObjectLiteral","nestedProperties":{"duplex":{"dataType":"string","required":true},"linkSpeed":{"dataType":"string","required":true},"portId":{"dataType":"string","required":true},"device":{"ref":"mongoose.Types.ObjectId","required":true}},"required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "CreateCableRequest": {
        "dataType": "refObject",
        "properties": {
            "device1": {"dataType":"string","required":true},
            "device2": {"dataType":"string","required":true},
            "cableType": {"dataType":"string","required":true},
            "interface1": {"dataType":"nestedObjectLiteral","nestedProperties":{"duplex":{"dataType":"string","required":true},"linkSpeed":{"dataType":"string","required":true},"portId":{"dataType":"string","required":true},"device":{"dataType":"string","required":true}},"required":true},
            "interface2": {"dataType":"nestedObjectLiteral","nestedProperties":{"duplex":{"dataType":"string","required":true},"linkSpeed":{"dataType":"string","required":true},"portId":{"dataType":"string","required":true},"device":{"dataType":"string","required":true}},"required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "UpdateCableRequest": {
        "dataType": "refObject",
        "properties": {
            "device1": {"dataType":"string"},
            "device2": {"dataType":"string"},
            "cableType": {"dataType":"string"},
            "interface1": {"dataType":"nestedObjectLiteral","nestedProperties":{"duplex":{"dataType":"string"},"linkSpeed":{"dataType":"string"},"portId":{"dataType":"string"},"device":{"dataType":"string"}}},
            "interface2": {"dataType":"nestedObjectLiteral","nestedProperties":{"duplex":{"dataType":"string"},"linkSpeed":{"dataType":"string"},"portId":{"dataType":"string"},"device":{"dataType":"string"}}},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "LoginResponse": {
        "dataType": "refObject",
        "properties": {
            "token": {"dataType":"string","required":true},
            "user": {"dataType":"nestedObjectLiteral","nestedProperties":{"role":{"dataType":"string","required":true},"name":{"dataType":"string","required":true}},"required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "LoginRequest": {
        "dataType": "refObject",
        "properties": {
            "email": {"dataType":"string","required":true},
            "password": {"dataType":"string","required":true},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
    "RegisterRequest": {
        "dataType": "refObject",
        "properties": {
            "name": {"dataType":"string","required":true},
            "email": {"dataType":"string","required":true},
            "password": {"dataType":"string","required":true},
            "role": {"dataType":"union","subSchemas":[{"dataType":"enum","enums":["guest"]},{"dataType":"enum","enums":["technician"]},{"dataType":"enum","enums":["admin"]}]},
        },
        "additionalProperties": false,
    },
    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
};
const templateService = new ExpressTemplateService(models, {"noImplicitAdditionalProperties":"throw-on-extras","bodyCoercion":true});

// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa




export function RegisterRoutes(app: Router) {

    // ###########################################################################################################
    //  NOTE: If you do not see routes for all of your controllers in this file, then you might not have informed tsoa of where to look
    //      Please look into the "controllerPathGlobs" config option described in the readme: https://github.com/lukeautry/tsoa
    // ###########################################################################################################


    
        const argsUserController_getUsers: Record<string, TsoaRoute.ParameterSchema> = {
                page: {"default":1,"in":"query","name":"page","dataType":"double"},
                limit: {"default":10,"in":"query","name":"limit","dataType":"double"},
        };
        app.get('/users',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(UserController)),
            ...(fetchMiddlewares<RequestHandler>(UserController.prototype.getUsers)),

            async function UserController_getUsers(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsUserController_getUsers, request, response });

                const controller = new UserController();

              await templateService.apiHandler({
                methodName: 'getUsers',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsUserController_getProfile: Record<string, TsoaRoute.ParameterSchema> = {
                req: {"in":"request","name":"req","required":true,"dataType":"object"},
        };
        app.get('/users/profile',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(UserController)),
            ...(fetchMiddlewares<RequestHandler>(UserController.prototype.getProfile)),

            async function UserController_getProfile(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsUserController_getProfile, request, response });

                const controller = new UserController();

              await templateService.apiHandler({
                methodName: 'getProfile',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsUserController_getUser: Record<string, TsoaRoute.ParameterSchema> = {
                userId: {"in":"path","name":"userId","required":true,"dataType":"string"},
        };
        app.get('/users/:userId',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(UserController)),
            ...(fetchMiddlewares<RequestHandler>(UserController.prototype.getUser)),

            async function UserController_getUser(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsUserController_getUser, request, response });

                const controller = new UserController();

              await templateService.apiHandler({
                methodName: 'getUser',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsUserController_createUser: Record<string, TsoaRoute.ParameterSchema> = {
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"CreateUserRequest"},
        };
        app.post('/users',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(UserController)),
            ...(fetchMiddlewares<RequestHandler>(UserController.prototype.createUser)),

            async function UserController_createUser(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsUserController_createUser, request, response });

                const controller = new UserController();

              await templateService.apiHandler({
                methodName: 'createUser',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsUserController_updateUser: Record<string, TsoaRoute.ParameterSchema> = {
                userId: {"in":"path","name":"userId","required":true,"dataType":"string"},
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"UpdateUserRequest"},
        };
        app.put('/users/:userId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(UserController)),
            ...(fetchMiddlewares<RequestHandler>(UserController.prototype.updateUser)),

            async function UserController_updateUser(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsUserController_updateUser, request, response });

                const controller = new UserController();

              await templateService.apiHandler({
                methodName: 'updateUser',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsUserController_deleteUser: Record<string, TsoaRoute.ParameterSchema> = {
                userId: {"in":"path","name":"userId","required":true,"dataType":"string"},
        };
        app.delete('/users/:userId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(UserController)),
            ...(fetchMiddlewares<RequestHandler>(UserController.prototype.deleteUser)),

            async function UserController_deleteUser(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsUserController_deleteUser, request, response });

                const controller = new UserController();

              await templateService.apiHandler({
                methodName: 'deleteUser',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsSiteController_getSites: Record<string, TsoaRoute.ParameterSchema> = {
                page: {"default":1,"in":"query","name":"page","dataType":"double"},
                limit: {"default":10,"in":"query","name":"limit","dataType":"double"},
        };
        app.get('/sites',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(SiteController)),
            ...(fetchMiddlewares<RequestHandler>(SiteController.prototype.getSites)),

            async function SiteController_getSites(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_getSites, request, response });

                const controller = new SiteController();

              await templateService.apiHandler({
                methodName: 'getSites',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsSiteController_getSite: Record<string, TsoaRoute.ParameterSchema> = {
                siteId: {"in":"path","name":"siteId","required":true,"dataType":"string"},
        };
        app.get('/sites/:siteId',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(SiteController)),
            ...(fetchMiddlewares<RequestHandler>(SiteController.prototype.getSite)),

            async function SiteController_getSite(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_getSite, request, response });

                const controller = new SiteController();

              await templateService.apiHandler({
                methodName: 'getSite',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsSiteController_createSite: Record<string, TsoaRoute.ParameterSchema> = {
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"CreateSiteRequest"},
        };
        app.post('/sites',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(SiteController)),
            ...(fetchMiddlewares<RequestHandler>(SiteController.prototype.createSite)),

            async function SiteController_createSite(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_createSite, request, response });

                const controller = new SiteController();

              await templateService.apiHandler({
                methodName: 'createSite',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsSiteController_updateSite: Record<string, TsoaRoute.ParameterSchema> = {
                siteId: {"in":"path","name":"siteId","required":true,"dataType":"string"},
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"UpdateSiteRequest"},
        };
        app.put('/sites/:siteId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(SiteController)),
            ...(fetchMiddlewares<RequestHandler>(SiteController.prototype.updateSite)),

            async function SiteController_updateSite(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_updateSite, request, response });

                const controller = new SiteController();

              await templateService.apiHandler({
                methodName: 'updateSite',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsSiteController_deleteSite: Record<string, TsoaRoute.ParameterSchema> = {
                siteId: {"in":"path","name":"siteId","required":true,"dataType":"string"},
        };
        app.delete('/sites/:siteId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(SiteController)),
            ...(fetchMiddlewares<RequestHandler>(SiteController.prototype.deleteSite)),

            async function SiteController_deleteSite(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsSiteController_deleteSite, request, response });

                const controller = new SiteController();

              await templateService.apiHandler({
                methodName: 'deleteSite',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsIpController_getIps: Record<string, TsoaRoute.ParameterSchema> = {
                page: {"default":1,"in":"query","name":"page","dataType":"double"},
                limit: {"default":10,"in":"query","name":"limit","dataType":"double"},
        };
        app.get('/ips',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(IpController)),
            ...(fetchMiddlewares<RequestHandler>(IpController.prototype.getIps)),

            async function IpController_getIps(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsIpController_getIps, request, response });

                const controller = new IpController();

              await templateService.apiHandler({
                methodName: 'getIps',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsIpController_getIp: Record<string, TsoaRoute.ParameterSchema> = {
                ipId: {"in":"path","name":"ipId","required":true,"dataType":"string"},
        };
        app.get('/ips/:ipId',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(IpController)),
            ...(fetchMiddlewares<RequestHandler>(IpController.prototype.getIp)),

            async function IpController_getIp(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsIpController_getIp, request, response });

                const controller = new IpController();

              await templateService.apiHandler({
                methodName: 'getIp',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsIpController_createIp: Record<string, TsoaRoute.ParameterSchema> = {
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"CreateIpRequest"},
        };
        app.post('/ips',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(IpController)),
            ...(fetchMiddlewares<RequestHandler>(IpController.prototype.createIp)),

            async function IpController_createIp(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsIpController_createIp, request, response });

                const controller = new IpController();

              await templateService.apiHandler({
                methodName: 'createIp',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsIpController_updateIp: Record<string, TsoaRoute.ParameterSchema> = {
                ipId: {"in":"path","name":"ipId","required":true,"dataType":"string"},
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"UpdateIpRequest"},
        };
        app.put('/ips/:ipId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(IpController)),
            ...(fetchMiddlewares<RequestHandler>(IpController.prototype.updateIp)),

            async function IpController_updateIp(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsIpController_updateIp, request, response });

                const controller = new IpController();

              await templateService.apiHandler({
                methodName: 'updateIp',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsIpController_deleteIp: Record<string, TsoaRoute.ParameterSchema> = {
                ipId: {"in":"path","name":"ipId","required":true,"dataType":"string"},
        };
        app.delete('/ips/:ipId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(IpController)),
            ...(fetchMiddlewares<RequestHandler>(IpController.prototype.deleteIp)),

            async function IpController_deleteIp(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsIpController_deleteIp, request, response });

                const controller = new IpController();

              await templateService.apiHandler({
                methodName: 'deleteIp',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsDeviceController_getDevices: Record<string, TsoaRoute.ParameterSchema> = {
                page: {"default":1,"in":"query","name":"page","dataType":"double"},
                limit: {"default":10,"in":"query","name":"limit","dataType":"double"},
        };
        app.get('/devices',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(DeviceController)),
            ...(fetchMiddlewares<RequestHandler>(DeviceController.prototype.getDevices)),

            async function DeviceController_getDevices(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_getDevices, request, response });

                const controller = new DeviceController();

              await templateService.apiHandler({
                methodName: 'getDevices',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsDeviceController_getDevice: Record<string, TsoaRoute.ParameterSchema> = {
                deviceId: {"in":"path","name":"deviceId","required":true,"dataType":"string"},
        };
        app.get('/devices/:deviceId',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(DeviceController)),
            ...(fetchMiddlewares<RequestHandler>(DeviceController.prototype.getDevice)),

            async function DeviceController_getDevice(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_getDevice, request, response });

                const controller = new DeviceController();

              await templateService.apiHandler({
                methodName: 'getDevice',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsDeviceController_createDevice: Record<string, TsoaRoute.ParameterSchema> = {
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"CreateDeviceRequest"},
        };
        app.post('/devices',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(DeviceController)),
            ...(fetchMiddlewares<RequestHandler>(DeviceController.prototype.createDevice)),

            async function DeviceController_createDevice(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_createDevice, request, response });

                const controller = new DeviceController();

              await templateService.apiHandler({
                methodName: 'createDevice',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsDeviceController_updateDevice: Record<string, TsoaRoute.ParameterSchema> = {
                deviceId: {"in":"path","name":"deviceId","required":true,"dataType":"string"},
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"UpdateDeviceRequest"},
        };
        app.put('/devices/:deviceId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(DeviceController)),
            ...(fetchMiddlewares<RequestHandler>(DeviceController.prototype.updateDevice)),

            async function DeviceController_updateDevice(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_updateDevice, request, response });

                const controller = new DeviceController();

              await templateService.apiHandler({
                methodName: 'updateDevice',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsDeviceController_deleteDevice: Record<string, TsoaRoute.ParameterSchema> = {
                deviceId: {"in":"path","name":"deviceId","required":true,"dataType":"string"},
        };
        app.delete('/devices/:deviceId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(DeviceController)),
            ...(fetchMiddlewares<RequestHandler>(DeviceController.prototype.deleteDevice)),

            async function DeviceController_deleteDevice(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsDeviceController_deleteDevice, request, response });

                const controller = new DeviceController();

              await templateService.apiHandler({
                methodName: 'deleteDevice',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsCableController_getCables: Record<string, TsoaRoute.ParameterSchema> = {
                page: {"default":1,"in":"query","name":"page","dataType":"double"},
                limit: {"default":10,"in":"query","name":"limit","dataType":"double"},
        };
        app.get('/cables',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(CableController)),
            ...(fetchMiddlewares<RequestHandler>(CableController.prototype.getCables)),

            async function CableController_getCables(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsCableController_getCables, request, response });

                const controller = new CableController();

              await templateService.apiHandler({
                methodName: 'getCables',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsCableController_getCable: Record<string, TsoaRoute.ParameterSchema> = {
                cableId: {"in":"path","name":"cableId","required":true,"dataType":"string"},
        };
        app.get('/cables/:cableId',
            authenticateMiddleware([{"jwt":[]}]),
            ...(fetchMiddlewares<RequestHandler>(CableController)),
            ...(fetchMiddlewares<RequestHandler>(CableController.prototype.getCable)),

            async function CableController_getCable(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsCableController_getCable, request, response });

                const controller = new CableController();

              await templateService.apiHandler({
                methodName: 'getCable',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsCableController_createCable: Record<string, TsoaRoute.ParameterSchema> = {
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"CreateCableRequest"},
        };
        app.post('/cables',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(CableController)),
            ...(fetchMiddlewares<RequestHandler>(CableController.prototype.createCable)),

            async function CableController_createCable(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsCableController_createCable, request, response });

                const controller = new CableController();

              await templateService.apiHandler({
                methodName: 'createCable',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsCableController_updateCable: Record<string, TsoaRoute.ParameterSchema> = {
                cableId: {"in":"path","name":"cableId","required":true,"dataType":"string"},
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"UpdateCableRequest"},
        };
        app.put('/cables/:cableId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(CableController)),
            ...(fetchMiddlewares<RequestHandler>(CableController.prototype.updateCable)),

            async function CableController_updateCable(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsCableController_updateCable, request, response });

                const controller = new CableController();

              await templateService.apiHandler({
                methodName: 'updateCable',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsCableController_deleteCable: Record<string, TsoaRoute.ParameterSchema> = {
                cableId: {"in":"path","name":"cableId","required":true,"dataType":"string"},
        };
        app.delete('/cables/:cableId',
            authenticateMiddleware([{"jwt":["admin"]}]),
            ...(fetchMiddlewares<RequestHandler>(CableController)),
            ...(fetchMiddlewares<RequestHandler>(CableController.prototype.deleteCable)),

            async function CableController_deleteCable(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsCableController_deleteCable, request, response });

                const controller = new CableController();

              await templateService.apiHandler({
                methodName: 'deleteCable',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: undefined,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsAuthController_login: Record<string, TsoaRoute.ParameterSchema> = {
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"LoginRequest"},
        };
        app.post('/auth/login',
            ...(fetchMiddlewares<RequestHandler>(AuthController)),
            ...(fetchMiddlewares<RequestHandler>(AuthController.prototype.login)),

            async function AuthController_login(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsAuthController_login, request, response });

                const controller = new AuthController();

              await templateService.apiHandler({
                methodName: 'login',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: 200,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
        const argsAuthController_register: Record<string, TsoaRoute.ParameterSchema> = {
                requestBody: {"in":"body","name":"requestBody","required":true,"ref":"RegisterRequest"},
        };
        app.post('/auth/register',
            ...(fetchMiddlewares<RequestHandler>(AuthController)),
            ...(fetchMiddlewares<RequestHandler>(AuthController.prototype.register)),

            async function AuthController_register(request: ExRequest, response: ExResponse, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            let validatedArgs: any[] = [];
            try {
                validatedArgs = templateService.getValidatedArgs({ args: argsAuthController_register, request, response });

                const controller = new AuthController();

              await templateService.apiHandler({
                methodName: 'register',
                controller,
                response,
                next,
                validatedArgs,
                successStatus: 201,
              });
            } catch (err) {
                return next(err);
            }
        });
        // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa


    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

    function authenticateMiddleware(security: TsoaRoute.Security[] = []) {
        return async function runAuthenticationMiddleware(request: any, response: any, next: any) {

            // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

            // keep track of failed auth attempts so we can hand back the most
            // recent one.  This behavior was previously existing so preserving it
            // here
            const failedAttempts: any[] = [];
            const pushAndRethrow = (error: any) => {
                failedAttempts.push(error);
                throw error;
            };

            const secMethodOrPromises: Promise<any>[] = [];
            for (const secMethod of security) {
                if (Object.keys(secMethod).length > 1) {
                    const secMethodAndPromises: Promise<any>[] = [];

                    for (const name in secMethod) {
                        secMethodAndPromises.push(
                            expressAuthenticationRecasted(request, name, secMethod[name], response)
                                .catch(pushAndRethrow)
                        );
                    }

                    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa

                    secMethodOrPromises.push(Promise.all(secMethodAndPromises)
                        .then(users => { return users[0]; }));
                } else {
                    for (const name in secMethod) {
                        secMethodOrPromises.push(
                            expressAuthenticationRecasted(request, name, secMethod[name], response)
                                .catch(pushAndRethrow)
                        );
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
            catch(err) {
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
        }
    }

    // WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
}

// WARNING: This file was auto-generated with tsoa. Please do not modify it. Re-run tsoa to re-generate this file: https://github.com/lukeautry/tsoa
