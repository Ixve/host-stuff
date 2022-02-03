import * as Express from 'express';
import AdminMiddleware from '@/middleware/v1/AdminMiddleware';
import UserRouter from '@/routes/v1/admin/UserRouter';

const AdminRouter: Express.Router = Express.Router();

AdminRouter.use(AdminMiddleware);
AdminRouter.use('/users', UserRouter);

export default AdminRouter;