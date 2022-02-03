import { object, string } from 'joi';

const UpdateEmail = () => {
    return object({
        password: string().required(),
        email: string().email().required(),
        confirm_email: string().email().required(),
    });
}; 

export { UpdateEmail };