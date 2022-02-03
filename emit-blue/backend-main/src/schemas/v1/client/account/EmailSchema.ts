import { object, string } from 'joi';

export default () => {
    return object({
        email: string()
            .email()
            .required(),
        confirm_email: string()
            .email()
            .required(),
        password: string()
            .required(),
    });
}; 