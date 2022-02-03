import * as Express from 'express';
import Prisma from '@/prisma';

const Index: Express.Handler = (request: Express.Request, response: Express.Response) => {
    Prisma.userSubdomain.findMany({
        where: {
            userId: request.user.id,
        },
    }).then(subdomains => {
        return response.status(200).json({
            status: 200,
            data: {
                subdomains: subdomains,
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
    const { subdomain_name } = request.body;
    
    Prisma.userSubdomain.create({
        data: {
           userId: request.user.id,
           name: subdomain_name,
        },
    }).then(() => {
        return response.status(200).json({
            status: 200,
            data: null,
            message: 'Successfully added the subdomain.',
        });
    });
};

export { Index, Store };