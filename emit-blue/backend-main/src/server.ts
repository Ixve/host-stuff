import * as Express from 'express';
import * as BodyParser from 'body-parser';
import { v1 } from '@/routes/index';
import * as Cors from 'cors';
import * as CookieParser from 'cookie-parser';
import * as ExpressSession from 'express-session';
import 'dotenv/config';

const App: Express.Application = Express();

App.use(ExpressSession({
    secret: process.env.APP_SESSION_SECRET,
    cookie: {
        sameSite: 'lax',
        httpOnly: true,
    },
    resave: false,
    saveUninitialized: false,
}));
App.use(BodyParser.json());
App.use(BodyParser.urlencoded({ extended: true }));
App.use(Cors({
    credentials: true,
    methods: [ 'GET', 'POST', 'DELETE' ],
    origin: process.env.APP_ALLOWED_ORIGIN,
}));
App.use('/v1', v1);

App.listen(parseInt(process.env.HTTP_PORT), process.env.HTTP_HOST, () => {
    console.log(`HTTP is listening on ${process.env.HTTP_HOST}:${process.env.HTTP_PORT}`);
});