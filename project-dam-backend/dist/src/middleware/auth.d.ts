import { Request, Response, NextFunction } from 'express';
import { IUser } from '../models/user';
declare global {
    namespace Express {
        interface Request {
            user?: IUser;
        }
    }
}
export declare function expressAuthentication(request: Request, securityName: string, scopes?: string[]): Promise<any>;
export declare const requireRole: (roles: string[]) => (req: Request, res: Response, next: NextFunction) => Response<any, Record<string, any>> | undefined;
export interface AuthenticatedRequest extends Request {
    user: IUser;
}
//# sourceMappingURL=auth.d.ts.map