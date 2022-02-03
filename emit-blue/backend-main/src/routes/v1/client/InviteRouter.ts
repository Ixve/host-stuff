import * as Express from 'express';
import * as InviteController from '@/controllers/v1/client/InviteController';

const InviteRouter: Express.Router = Express.Router();

InviteRouter.get('/', InviteController.Index);
InviteRouter.post('/', InviteController.Store);

export default InviteRouter;