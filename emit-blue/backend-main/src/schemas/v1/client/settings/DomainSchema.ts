import { object, string } from 'joi';

export default () => {
    return object({
        domain_name: string()
            .required()
            .max(64),
    });
}; 