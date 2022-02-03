import * as Express from 'express';
import Prisma from '@/prisma';

const Store: Express.Handler = (request: Express.Request, response: Express.Response) => {
    const { embed_name, embed_name_url, embed_author, embed_author_url, embed_title, embed_description, embed_colour } = request.body;

    Prisma.user.update({
        where: {
            id: request.user.id,
        },
        data: {
            embedName: embed_name || '',
            embedNameUrl: embed_name_url || '',
            embedAuthor: embed_author || '',
            embedAuthorUrl: embed_author_url || '',
            embedTitle: embed_title || '',
            embedDesc: embed_description || '',
            embedColour: embed_colour || '',
        },
    }).then(user => {
        if (!user) return response.status(500).json({
            status: 500,
            data: null,
            message: "Something went wrong.",
        });

        return response.status(200).json({
            status: 200,
            data: {
                embed: {
                    embed_name: user.embedName,
                    embed_name_url: user.embedNameUrl,
                    embed_author: user.embedAuthor,
                    embed_author_url: user.embedAuthorUrl,
                    embed_title: user.embedTitle,
                    embed_description: user.embedDesc,
                    embed_colour: user.embedColour,
                },
            },
            message: 'Successfully updated your embed configuration.',
        });
    }).catch(() => {
        return response.status(500).json({
            status: 500,
            data: null,
            message: "Something went wrong.",
        });
    });
};

export { Store };