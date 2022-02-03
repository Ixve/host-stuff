import { object, string } from 'joi';

export default () => {
    return object({
        username: string().required().max(64),
        password: string().required(),
        email: string().email().required(),
        invite_code: string().required(),
    });
}; 