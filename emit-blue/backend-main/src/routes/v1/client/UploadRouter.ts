import * as Express from 'express';
import TokenMiddleware from '@/middleware/v1/TokenMiddleware';
import * as UploadController from '@/controllers/v1/client/UploadController';

const UploadRouter: Express.Router = Express.Router();

UploadRouter.use(TokenMiddleware);
UploadRouter.post('/', UploadController.Store);

export default UploadRouter;

