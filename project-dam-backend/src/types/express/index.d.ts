// src/types/express/index.d.ts
import { IUser } from '../../models/user';
import { ISite } from '../../models/site';
import { IDevice } from '../../models/device';
import { ICable } from '../../models/cable';
import { IIp } from '../../models/ip';

declare global {
	namespace Express {
		interface Request {
			user?: IUser;
			site?: ISite;
			device?: IDevice;
			cable?: ICable;
			ip?: IIp;
		}
	}
}