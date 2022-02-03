import * as Express from 'express';
import ClientMiddleware from '@/middleware/v1/ClientMiddleware';
import BaseRouter from '@/routes/v1/client/BaseRouter';
import InviteRouter from '@/routes/v1/client/InviteRouter';
import AccountRouter from '@/routes/v1/client/AccountRouter';
import SettingsRouter from '@/routes/v1/client/SettingsRouter';
import UploadRouter from '@/routes/v1/client/UploadRouter';

const ClientRouter: Express.Router = Express.Router();

ClientRouter.use('/upload', UploadRouter);
ClientRouter.use(ClientMiddleware);
ClientRouter.use('/', BaseRouter);
ClientRouter.use('/invites', InviteRouter);
ClientRouter.use('/account', AccountRouter);
ClientRouter.use('/settings', SettingsRouter);

export default ClientRouter;