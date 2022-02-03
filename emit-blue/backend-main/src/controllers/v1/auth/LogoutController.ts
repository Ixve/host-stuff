import * as Express from 'express';

const Main : Express.Handler = async (_request: Express.Request, response: Express.Response) => {
    response.clearCookie("connect.sid")
    return response.json({ok:true,message:"Logged out successfuly"})
}

export { Main };
