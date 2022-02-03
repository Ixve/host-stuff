import * as Express from 'express';
import { generateRandom } from '@/utils/v1/GeneratorUtil';
import Prisma from '@/prisma';

const Store: Express.Handler = (request: Express.Request, response: Express.Response) => {
    if (request.user.userDomains.length <= 0) {
        return response.status(403).json({
            status: 403,
            data: null,
            message: 'You must have at least one domain.',
        });
    }

    if (request.user.userSubdomains.length <= 0) {
        return response.status(403).json({
            status: 403,
            data: null,
            message: 'You must have at least one subdomain.',
        });
    }
};

export { Store };