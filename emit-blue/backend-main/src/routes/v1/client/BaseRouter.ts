import * as Express from 'express';
import * as BaseController from '@/controllers/v1/client/BaseController';

const BaseRouter: Express.Router = Express.Router();

BaseRouter.get('/', BaseController.Index);

export default BaseRouter;