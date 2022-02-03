import * as Express from 'express';
import Prisma from '@/prisma';

const Index: Express.Handler = (request: Express.Request, response: Express.Response) => {
    const { user } = request;

    return response.json({
        status: 200,
        data: {
            user: {
                email: user.email,
                username: user.username,
                apiKey: user.apiKey,
                uuid: user.uuid,
                id: user.id,
            }
        },
        message: null,
    });
};

export { Index };