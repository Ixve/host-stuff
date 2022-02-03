import * as Express from 'express';
import * as Argon from 'argon2';
import Prisma from '@/prisma';

const Store: Express.Handler = (request: Express.Request, response: Express.Response) => {
    const { email, confirm_email, password } = request.body;
    
    if (email !== confirm_email) return response.status(403).json({
        status: 422,
        data: null,
        message: 'The provided email must be same as email confirmation.',
    });

    Argon.verify(request.user.password, password).then((result) => {
        if (!result) return response.status(403).json({
            status: 403,
            data: null,
            message: 'The provided password do not match our records.',
        });
    
        Prisma.user.update({
            where: {
                id: request.user.id,
            },
            data: {
                email: email,
            },
        }).then(user => {
            return response.json({
                status: 200,
                data: {
                    user: {
                        email: user.email,
                    },
                },
                message: 'Successfully changed your email.',
            });
        }).catch(() => {
            return response.json({
                status: 500,
                data: null,
                message: 'Something went wrong.',
            });
        });
    });
};

export { Store };