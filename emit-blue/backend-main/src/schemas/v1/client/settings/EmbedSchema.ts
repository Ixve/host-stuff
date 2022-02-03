import { object, string } from 'joi';

export default () => {
    return object({
        embed_name: string()
            .optional()
            .max(64),
        embed_name_url: string()
            .optional()
            .max(64),
        embed_author: string()
            .optional()
            .max(64),
        embed_author_url: string()
            .optional()
            .max(64),
        embed_title: string()
            .optional()
            .max(64),
        embed_description: string()
            .optional()
            .max(64),
        embed_colour: string()
            .required()
            .regex(/^#[A-Fa-f0-9]{6}/)
            .max(64),
    });
}; 