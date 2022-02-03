import * as Express from 'express';
import Prisma from '@/prisma';

const GetDomains: Express.Handler = (request: Express.Request, response: Express.Response) => {
    Prisma.userDomain.findMany({
        where: {
            userId: request.user.id,
        },
        include: {
            domain: true,
        },
    }).then(domains => {
        return response.status(200).json({
            status: 200,
            data: {
                domains: domains,
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

const AddDomain: Express.Handler = (request: Express.Request, response: Express.Response) => {
    const { domain_name } = request.body;

    Prisma.domain.findFirst({
        where: {
            name: domain_name,
        },
    }).then(domain => {
        if (!domain) return response.status(404).json({
            status: 404,
            data: null,
            message: 'Could not find the provided domain.',
        });

        Prisma.userDomain.create({
            data: {
                domainId: domain.id,
                userId: request.user.id,
            },
        }).then(() => {
            return response.status(200).json({
                status: 200,
                data: null,
                message: 'Successfully added the domain.',
            });
        });
    });
};

const GetSubdomains: Express.Handler = (request: Express.Request, response: Express.Response) => {
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

const AddSubdomain: Express.Handler = (request: Express.Request, response: Express.Response) => {
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

export { GetDomains, AddDomain, GetSubdomains, AddSubdomain };