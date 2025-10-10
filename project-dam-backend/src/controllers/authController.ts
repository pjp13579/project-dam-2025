import {
	Controller,
	Post,
	Route,
	Body,
	SuccessResponse,
	Tags
} from 'tsoa';
import { User, IUser } from '../models/user';
import jwt from 'jsonwebtoken';

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

@Route('auth')
@Tags('Authentication')
export class AuthController extends Controller {
	/**
	 * obtain JWT token
	 */
	@Post('login')
	@SuccessResponse('200', 'Successfully logged in')
	public async login(@Body() requestBody: LoginRequest): Promise<LoginResponse> {
		
		const { email, password } = requestBody;

		
		const user = await User.findOne({ email, isActive: true });
		if (!user) {
			throw new Error('Invalid credentials');
		}

		console.log(user);
		
		
		const isMatch = await user.comparePassword(password);
		if (!isMatch) {
			console.log("Password does not match");
			throw new Error('Invalid credentials');
		}

		
		const token = jwt.sign(
			{ userId: user.id },
			process.env.JWT_SECRET as string,
			{ expiresIn: "24h" }
		);

		return {
			token,
			user: {
				name: user.name,
				role: user.role
			}
		};
	}

	/**
	 * register users
	 */
	@Post('register')
	@SuccessResponse('201', 'User created successfully')
	public async register(@Body() requestBody: RegisterRequest): Promise<{ message: string; userId: string; }> {
		try {
			const existingUser = await User.findOne({ email: requestBody.email });
			if (existingUser) {
				throw new Error('User already exists with this email');
			}

			
			const user = new User(requestBody);
			await user.save();

			this.setStatus(201);
			return {
				message: 'User created successfully',
				userId: user.id
			};
		} catch (error: any) {
			throw new Error(error.message);
		}
	}
}