import { Controller } from 'tsoa';
import { IUser } from '../models/user';
import { AuthenticatedRequest } from '../middleware/auth';
interface CreateUserRequest {
    name: string;
    email: string;
    password: string;
    role: 'guest' | 'technician' | 'admin';
}
interface UpdateUserRequest {
    name?: string;
    email?: string;
    role?: 'guest' | 'technician' | 'admin';
    isActive?: boolean;
}
export declare class UserController extends Controller {
    /**
     * Get all users (Admin only)
     */
    getUsers(page?: number, limit?: number): Promise<{
        users: IUser[];
        total: number;
        pages: number;
    }>;
    /**
     * Get current user profile
     */
    getProfile(req: AuthenticatedRequest): Promise<IUser>;
    /**
     * Get user by ID
     */
    getUser(userId: string): Promise<IUser>;
    /**
     * Create a new user (Admin only)
     */
    createUser(requestBody: CreateUserRequest): Promise<IUser>;
    /**
     * Update user (Admin only)
     */
    updateUser(userId: string, requestBody: UpdateUserRequest): Promise<IUser>;
    /**
     * Delete user (Admin only)
     */
    deleteUser(userId: string): Promise<{
        message: string;
    }>;
}
export {};
//# sourceMappingURL=userController.d.ts.map