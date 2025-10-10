import { Controller } from 'tsoa';
interface LoginRequest {
    email: string;
    password: string;
}
interface LoginResponse {
    token: string;
    user: {
        name: string;
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
     * obtain JWT token
     */
    login(requestBody: LoginRequest): Promise<LoginResponse>;
    /**
     * register users
     */
    register(requestBody: RegisterRequest): Promise<{
        message: string;
        userId: string;
    }>;
}
export {};
//# sourceMappingURL=authController.d.ts.map