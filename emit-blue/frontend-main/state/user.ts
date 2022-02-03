import * as EasyPeasy from 'easy-peasy';

export interface IUser {
    id: number;
    email: string;
    username: string;
};

export interface IUserStore {
    data?: IUser;
    updateUser: EasyPeasy.Action<IUserStore, Partial<IUser>>;
    setUser: EasyPeasy.Action<IUserStore, IUser | undefined>;
};

const User: IUserStore = {
    data: undefined,
    updateUser: EasyPeasy.action((state, payload) => {
        // @ts-ignore
        state.data = { ...state.data, ...payload };
    }),
    setUser: EasyPeasy.action((state, payload) => {
        state.data = payload;
    }),
};

export default User;