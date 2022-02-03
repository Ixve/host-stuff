import * as Express from 'express';
import * as LoginController from '@/controllers/v1/auth/LoginController';
import * as RegisterController from '@/controllers/v1/auth/RegisterController';
import * as LogoutController from '@/controllers/v1/auth/LogoutController';
import RegisterSchema from '@/schemas/v1/auth/RegisterSchema';
import SchemaMiddleware from '@/middleware/v1/SchemaMiddleware';

const AuthRouter: Express.Router = Express.Router();

AuthRouter.post('/login', LoginController.Authenticate);
AuthRouter.post('/register', SchemaMiddleware(RegisterSchema()), RegisterController.Store);
AuthRouter.post('/logout', LogoutController.Main);

export default AuthRouter;