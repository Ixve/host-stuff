import * as Express from 'express';
import { generateRandom } from '@/utils/v1/GeneratorUtil';
import Prisma from '@/prisma';

const Index: Express.Handler = (request: Express.Request, response: Express.Response) => {
    Prisma.invite.findMany({
        where: {
            userId: request.user.id,
        },
    }).then(invites => {
        return response.status(200).json({
            status: 200,
            data: {
                invites: invites,
            },
            message: null,
        });
    }).catch(() => {
        return response.status(500).json({
            status: 500,
            data: null,
            message: "Something went wrong.",
        });
    });
};

const Store: Express.Handler = (request: Express.Request, response: Express.Response) => {
    if (request.user.availableInvites <= 0) {
        return response.status(403).json({
            status: 403,
            data: null,
            message: 'You cannot generate an invite.',
        });
    }

    Prisma.user.update({
        where: {
            id: request.user.id,
        },
        data: {
            availableInvites: request.user.availableInvites - 1,
        },
    }).then(() => {
        Prisma.invite.create({
            data: {
                userId: request.user.id,
                code: generateRandom(32),
            },
        }).then(invite => {
            return response.status(200).json({
                status: 200,
                data: {
                    invite: invite,
                },
                message: 'Successfully generated an invite.',
            });
        }).catch(() => {
            return response.status(500).json({
                status: 500,
                data: null,
                message: "Something went wrong.",
            });
        });
    });
};
export { Index, Store };