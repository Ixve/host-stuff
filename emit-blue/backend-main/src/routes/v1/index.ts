import * as Express from 'express';
import BaseRouter from '@/routes/v1/BaseRouter';
import AuthRouter from '@/routes/v1/AuthRouter';
import AdminRouter from '@/routes/v1/AdminRouter';
import ClientRouter from '@/routes/v1/ClientRouter';

const v1: Express.Router = Express.Router();

v1.use('/', BaseRouter);
v1.use('/auth', AuthRouter);
v1.use('/client', ClientRouter);
v1.use('/admin', AdminRouter);

export default v1;