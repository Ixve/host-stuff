import * as Express from 'express';
import * as Argon from 'argon2';
import Prisma from '@/prisma';

const Store: Express.Handler = (request: Express.Request, response: Express.Response) => {
    const { password, confirm_password, old_password } = request.body;
    
    if (password !== confirm_password) return response.status(403).json({
        status: 422,
        data: null,
        message: 'The provided password must be same as password confirmation.',
    });

    Argon.verify(request.user.password, old_password).then((result) => {
        if (!result) return response.status(403).json({
            status: 403,
            data: null,
            message: 'The provided password do not match our records.',
        });
    
        Argon.hash(password).then(hash => {
            Prisma.user.update({
                where: {
                    id: request.user.id,
                },
                data: {
                    password: hash,
                },
            }).then(user => {
                return response.json({
                    status: 200,
                    data: null,
                    message: 'Successfully changed your password.',
                });
            }).catch(() => {
                return response.json({
                    status: 500,
                    data: null,
                    message: 'Something went wrong.',
                });
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