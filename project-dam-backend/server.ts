import express from 'express';
import mongoose from 'mongoose';
import cors from 'cors';
import dotenv from 'dotenv';
import { RegisterRoutes } from './dist/routes';
import swaggerUi from 'swagger-ui-express';
import { errorHandler } from './src/middleware/errorHandler';
import { expressAuthentication } from './src/middleware/auth';
import path from 'path';

// load .env
dotenv.config();

// create and config express app
const app = express();
const PORT = process.env.PORT || 5000;

// middleware
app.use(cors());
app.use(express.json());

// serve swagger ui
app.use('/docs', swaggerUi.serve, async (_req: express.Request, res: express.Response) => {
	return res.send(swaggerUi.generateHTML(await import('./dist/swagger.json'))); // "resolveJsonModule": true
});

// apply auth middleware to all routes except auth and docs
app.use((req, res, next) => {
	if (req.path === '/auth/login' || req.path.startsWith('/docs') || req.path.startsWith('/privacy-policy')) {
		return next();
	}
	expressAuthentication(req, 'jwt')
		.then(user => {
			// Optionally attach user to request
			(req as any).user = user;
			next();
		})
		.catch(err => {
			res.status(401).json({ message: err.message });
		});
});

// serve privacy policy
app.get("/privacy-policy", (req, res) => {
  res.sendFile(path.join(__dirname, "../public/privacy.html"));
});

// register tsoa routes
RegisterRoutes(app);

// error handling middleware
app.use(errorHandler);

console.log("Connecting to MongoDB...");
console.log("proc ess.env.MONGODB_URI: "+ process.env.MONGODB_URI);
console.log("proc ess.env.MONGODB_URI as string: "+ (process.env.MONGODB_URI as string));
// mongoDB connection
mongoose.connect(process.env.MONGODB_URI as string)
	.then(() => console.log('MongoDB connected successfully'))
	.catch(err => console.error('MongoDB connection error:', err));

// start server
app.listen(PORT, () => {
	console.log(`CacoRedes API server is running on port ${PORT}`);
	console.log(`API documentation available at http://localhost:${PORT}/docs`);
});
