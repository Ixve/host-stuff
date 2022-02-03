import { object, string } from 'joi';

export default () => {
    return object({
        subdomain_name: string()
            .required()
            .max(64),
    });
}; 