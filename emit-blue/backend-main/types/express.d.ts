import 'express';
import 'express-session';

declare module 'express' {
    export interface Request {
        user: any;
    }
}

declare module 'express-session' {
    export interface SessionData {
        userId: number;
    }
}