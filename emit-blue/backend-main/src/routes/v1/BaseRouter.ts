import * as Express from 'express';

const BaseRouter: Express.Router = Express.Router();

BaseRouter.get('/', (request, response) => {
    return response.json({
        status: 200,
        message: 'Hello there, you are very sus.',
    });
});

export default BaseRouter;