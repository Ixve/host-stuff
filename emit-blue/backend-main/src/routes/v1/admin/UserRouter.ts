import * as Express from 'express';
import * as UserController from '@/controllers/v1/admin/UserController';

const UserRouter: Express.Router = Express.Router();

UserRouter.get('/', UserController.Index);

export default UserRouter;