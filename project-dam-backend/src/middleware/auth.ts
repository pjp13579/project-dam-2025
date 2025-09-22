// src/middleware/auth.ts
import { Request, Response, NextFunction } from 'express';
import jwt from 'jsonwebtoken';
import { User, IUser } from '../models/user';

// extend the express request interface to include user
declare global {
	namespace Express {
		interface Request {
			user?: IUser;
		}
	}
}

// auth middleware
// verifies JWT token and attaches user to request
export async function expressAuthentication(
	request: Request,
	securityName: string,
	scopes?: string[]
): Promise<any> {
	if (securityName !== 'jwt') {
		throw new Error('Unsupported security name');
	}

	// authenticate using your existing logic
	const token = request.header('Authorization')?.replace('Bearer ', '');

	if (!token) {
		throw new Error('Access denied. No token provided.');
	}

	const decoded = jwt.verify(token, process.env.JWT_SECRET as string) as any;
	const user = await User.findById(decoded.userId).select('-password');

	if (!user) {
		throw new Error('Token is not valid.');
	}

	// check scopes if provided
	if (scopes && scopes.length > 0) {
		if (!scopes.includes(user.role)) {
			throw new Error('Access denied. Insufficient permissions.');
		}
	}

	return user;
}

export const requireRole = (roles: string[]) => {
	return (req: Request, res: Response, next: NextFunction) => {
		if (!req.user || !roles.includes(req.user.role)) {
			return res.status(403).json({
				message: 'Access denied. Insufficient permissions.'
			});
		}
		next();
	};
};

// user associated with request's JWT token
export interface AuthenticatedRequest extends Request {
	user: IUser;
}

