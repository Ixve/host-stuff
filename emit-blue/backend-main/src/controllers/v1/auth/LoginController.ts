import * as Express from 'express';
import * as JWT from 'jsonwebtoken';
import * as Argon from 'argon2';
import Prisma from '@/prisma';

const Authenticate: Express.Handler = (request: Express.Request, response: Express.Response) => {
    const { username, password } = request.body;

    if (!username || !password) return response.status(422).json({
        status: 422,
        data: null,
        message: 'Missing required fields.',
    });

    Prisma.user.findFirst({
        where: {
            username: username,
        },
    }).then(user => {
        if (!user) {
            return response.status(404).json({
                status: 404,
                data: null,
                message: 'The provided credentials do not match our records.',
            });
        }
        
        Argon.verify(user.password, password).then((result) => {
            if (!result) return response.status(403).json({
                status: 403,
                data: null,
                message: 'The provided credentials do not match our records.',
            });

            request.session.userId = user.id;

            return response.status(200).json({
                status: 200,
                data: null,
                message: 'Successfully logged in.',
            });
        });
    }).catch(() => {
        return response.status(500).json({
            status: 500,
            data: null,
            message: 'Something went wrong while logging in.',
        });
    });
};

export { Authenticate };