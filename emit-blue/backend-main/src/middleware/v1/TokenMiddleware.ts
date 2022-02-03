import * as Express from 'express';
import Prisma from '@/prisma';

export default (request: Express.Request, response: Express.Response, next: Express.NextFunction) => {
    const apiKey = request.headers['authorization'];

    if (!apiKey) {
        return response.status(401).json({
            status: 401,
            data: null,
            message: 'Unauthorized.',
        });
    }

    Prisma.user.findFirst({
        where: {
            apiKey: apiKey,
        },
        include: {
            userDomains: true,
            userSubdomains: true,
        },
    }).then(user => {
        request.user = user;
        
        next();
    }).catch(() => {
        return response.status(500).json({
            status: 500,
            data: null,
            message: 'Something went wrong.',
        });
    });
};