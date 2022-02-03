import * as Express from 'express';
import * as Argon from 'argon2';
import { generateRandom } from '@/utils/v1/GeneratorUtil';
import Prisma from '@/prisma';

const Store: Express.Handler = (request: Express.Request, response: Express.Response) => {
    const { username, email, password, invite_code } = request.body;

    Prisma.invite.findFirst({
        where: {
            code: invite_code,
        },
    }).then(invite => {
        if (!invite) return response.status(404).json({
            status: 404,
            data: null,
            message: 'The provided invite code could not be found.',
        });

        Argon.hash(password).then(hash => {
            Prisma.user.create({
                data: {
                    username: username,
                    email: email,
                    password: hash,
                    apiKey: generateRandom(64),
                },
            }).then(user => {
                if (!user) return response.status(500).json({
                    status: 500,
                    data: null,
                    message: 'Something went wrong.',
                });

                Prisma.invite.update({
                    where: {
                        id: invite.id
                    },
                    data: {
                        usedById: user.id,
                    },
                }).then(() => {
                    return response.status(200).json({
                        status: 200,
                        data: null,
                        message: 'Successfully created the account.',
                    });
                }).catch(() => {
                    return response.status(500).json({
                        status: 500,
                        data: null,
                        message: 'Something went wrong.',
                    });
                });
            }).catch(() => {
                return response.status(500).json({
                    status: 500,
                    data: null,
                    message: 'Something went wrong.',
                });
            });
        }).catch(() => {
            return response.status(500).json({
                status: 500,
                data: null,
                message: 'Something went wrong.',
            });
        });
    }).catch(() => {
        return response.status(500).json({
            status: 500,
            data: null,
            message: 'Something went wrong.',
        });
    });
};

export { Store };