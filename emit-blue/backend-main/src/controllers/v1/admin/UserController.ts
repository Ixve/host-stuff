import * as Express from 'express';
import Prisma from '@/prisma';

const Index: Express.Handler = (request: Express.Request, response: Express.Response) => {
    Prisma.user.findMany().then(users => {
        return response.json({
            status: 200,
            data: users,
            message: null,
        });
    }).catch(() => {
        return response.json({
            status: 500,
            data: null,
            message: "Something went wrong.",
        });
    });
};

export { Index };