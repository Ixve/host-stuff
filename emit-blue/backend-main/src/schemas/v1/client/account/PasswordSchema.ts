import { object, string } from 'joi';

export default () => {
    return object({
        password: string()
            .required(),
        confirm_password: string()
            .required(),
        old_password: string()
            .required(),
    });
}; 