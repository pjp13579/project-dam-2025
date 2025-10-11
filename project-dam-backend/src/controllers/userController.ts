import {
	Controller,
	Get,
	Post,
	Put,
	Delete,
	Route,
	Query,
	Body,
	Path,
	Security,
	Tags,
	Request
} from 'tsoa';
import { User, IUser } from '../models/user';
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

@Route('users')
@Tags('Users')
@Security('jwt')
export class UserController extends Controller {
	/**
	 * Get all users (Admin only)
	 */
	@Get()
	public async getUsers(
		@Query() page: number = 1,
		@Query() limit: number = 10
	): Promise<{ users: GetUser[]; total: number; pages: number; }> {
		const skip = (page - 1) * limit;
		const users = await User.find()
			.select('-password')
			.skip(skip)
			.limit(limit)
			.sort({ createdAt: -1 });

		const total = await User.countDocuments();

		// Map IUser objects to GetUser DTO
		const userDtos: GetUser[] = users.map(user => ({
			email: user.email,
			name: user.name,
			role: user.role
		}));

		return {
			users: userDtos,
			total,
			pages: Math.ceil(total / limit)
		};
	}

	/**
	 * Get current user profile
	 */
	@Get('profile')
	public async getProfile(@Request() req: AuthenticatedRequest): Promise<GetUser> {
		if (!req.user) {
			throw new Error('User not found');
		}

		const user = await User.findById(req.user._id).select('-password');
		if (!user) {
			throw new Error('User not found');
		}

		return {
			"email": user.email,
			"name": user.name,
			"role": user.role
		};
		
	}

	/**
	 * Get user by ID
	 */
	@Get('{userId}')
	public async getUser(@Path() userId: string): Promise<GetUser> {
		const user = await User.findById(userId).select('-password');
		if (!user) {
			throw new Error('User not found');
		}

		return {
			"email": user.email,
			"name": user.name,
			"role": user.role
		};
	}

	/**
	 * Create a new user (Admin only)
	 */
	@Post()
	@Security('jwt', ['admin'])
	public async createUser(@Body() requestBody: CreateUserRequest): Promise<IUser> {
		try {
			// Check if user already exists
			const existingUser = await User.findOne({ email: requestBody.email });
			if (existingUser) {
				throw new Error('User already exists with this email');
			}

			const user = new User(requestBody);
			user.createdAt = new Date();
			user.updatedAt = new Date();
			await user.save();

			// Return user without password
			const savedUser = await User.findById(user._id).select('-password');
			if (!savedUser) {
				throw new Error('Failed to create user');
			}

			this.setStatus(201);
			return savedUser;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	/**
	 * Update user (Admin only)
	 */
	@Put('{userId}')
	@Security('jwt', ['admin'])
	public async updateUser(
		@Path() userId: string,
		@Body() requestBody: UpdateUserRequest
	): Promise<IUser> {
		try {
			const user = await User.findByIdAndUpdate(
				userId,
				{ ...requestBody },
				{ new: true, runValidators: true }
			).select('-password');

			if (!user) {
				throw new Error('User not found');
			}

			return user;
		} catch (error: any) {
			throw new Error(error.message);
		}
	}

	/**
	 * Delete user (Admin only)
	 */
	@Delete('{userId}')
	@Security('jwt', ['admin'])
	public async deleteUser(@Path() userId: string): Promise<{ message: string; }> {
		const user = await User.findByIdAndUpdate(
			userId,
			{ isActive: false },
			{ new: true, runValidators: true }
		);

		if (!user) {
			throw new Error('User not found');
		}

		return { message: 'User deleted successfully' };
	}
}