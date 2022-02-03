import * as Express from 'express';
import { ArraySchema, ObjectSchema } from 'joi';

export default (schema: ArraySchema | ObjectSchema) => {
    return (request: Express.Request, response: Express.Response, next: Express.NextFunction) => {
        schema.validateAsync(request.body).then(() => {
            next();
        }).catch(error => {
            return response.status(422).json({
                status: 422,
                data: null,
                message: error.details.map(i => i.message).join(','),
            });
        });
    };
};