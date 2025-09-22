import { Controller } from 'tsoa';
interface LoginRequest {
    email: string;
    password: string;
}
interface LoginResponse {
    token: string;
    user: {
        id: string;
        name: string;
        email: string;
        role: string;
    };
}
interface RegisterRequest {
    name: string;
    email: string;
    password: string;
    role?: 'guest' | 'technician' | 'admin';
}
export declare class AuthController extends Controller {
    /**
     * Login to the system
     */
    login(requestBody: LoginRequest): Promise<LoginResponse>;
    /**
     * Register a new user (admin only)
     */
    register(requestBody: RegisterRequest): Promise<{
        message: string;
        userId: string;
    }>;
}
export {};
//# sourceMappingURL=authController.d.ts.map