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
interface GetUser {
    email: string;
    name: string;
    role: string;
}
export declare class UserController extends Controller {
    /**
     * Get all users (Admin only)
     */
    getUsers(page?: number, limit?: number): Promise<{
        users: GetUser[];
        total: number;
        pages: number;
    }>;
    /**
     * Get current user profile
     */
    getProfile(req: AuthenticatedRequest): Promise<GetUser>;
    /**
     * Get user by ID
     */
    getUser(userId: string): Promise<GetUser>;
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