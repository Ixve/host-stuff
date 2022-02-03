import * as EasyPeasy from 'easy-peasy';
import User, { IUserStore } from './user';

export interface ApplicationStore {
    user: IUserStore;
};

const State: ApplicationStore = {
    user: EasyPeasy.persist(User),
};

export const Store = EasyPeasy.createStore(State);