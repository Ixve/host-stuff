import { useContext, createContext } from 'react';
import * as API from '../api/old';

const UserContext = createContext<IUserContext>({} as IUserContext);

export interface IUser {
    api: API.default;
    username: string;
    email: string;
    apiKey: string;
}

export interface IUserContext {
    user: IUser;
    setUser(user: IUser): void;
};

export const UserProvider = ({ value, children }: any) => {
    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    );
};

export const useUser = () => useContext<IUserContext>(UserContext);
