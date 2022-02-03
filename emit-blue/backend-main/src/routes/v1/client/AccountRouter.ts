import * as Express from 'express';
import * as EmailController from '@/controllers/v1/client/account/EmailController';
import * as PasswordController from '@/controllers/v1/client/account/PasswordController';
import SchemaMiddleware from '@/middleware/v1/SchemaMiddleware';
import EmailSchema from '@/schemas/v1/client/account/EmailSchema';
import PasswordSchema from '@/schemas/v1/client/account/PasswordSchema';

const AccountRouter: Express.Router = Express.Router();

AccountRouter.post('/email', SchemaMiddleware(EmailSchema()), EmailController.Store);
AccountRouter.post('/password', SchemaMiddleware(PasswordSchema()), PasswordController.Store);

export default AccountRouter;