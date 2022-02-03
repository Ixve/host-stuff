import * as Axios from 'axios';
import { Store } from '../state';
import { IUser } from '../state/user';

const Http: Axios.AxiosInstance = Axios.default.create({
    baseURL: process.env.NEXT_PUBLIC_APP_BACKEND_URL,
    withCredentials: true,
});

Http.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        console.log(error.response);
        if (error.response?.status === 401) {
            Store.getActions().user.setUser(undefined);
            Store.persist.clear().then(() => {
                console.log('cleared!');
            });
        }

        return Promise.reject(error);
    },
);

function Login(username: string, password: string): Promise<IUser> {
    return new Promise((resolve, reject) => {
        Http.post('/auth/login', {
            username: username,
            password: password,
        }).then(response => {
            console.log('api called!');
            if (!response) {
                console.log('Null response')
                console.log(response)
                return reject({error:'Something went wrong...'})
            }
            if (!response?.data?.data) {
                if(response?.data.message == 'User not found'){
                    reject({error:'user not found'})
                }

                console.log("bruh")
                return reject({error:'Something went wrong...'})
            }
            Store.getActions().user.setUser(response?.data?.data?.user);

            return resolve(response?.data?.data.user);
        }).catch(error => {
            if(!error?.data) {
                return reject(error)
            }
            if(error?.data?.message) {
                reject({error:error.data.message})
            }
            console.log(error.data)
        });
    });
}

function freshData(): Promise<IUser> {
    return new Promise((resolve, reject) => {
        Http.get('/client').then(response => {
            console.log('api called!');
            Store.getActions().user.updateUser(response.data.data.user);

            return resolve(response.data.data.user);
        }).catch(error => {
            return reject(error);
        });
    });
}

function UpdateEmail(password: string, email: string, confirm_email: string): Promise<any> {
    return new Promise((resolve, reject) => {
        Http.post('/client/account/email', {
            password: password,
            email: email,
            confirm_email: confirm_email,
        }).then(response => {
            if (!response) {
                console.log('Null response')
                return reject({error:'Something went wrong...'});
            }

            if (!response.data.message) {
                console.log("bruhs")
                return reject({error:'Something went wrong...'});
            }

            console.log(response.data);

            resolve(response.data);
        }).catch(error => {
            console.log('bro wtf');
            if(!error?.data) {
                return reject(error);
            }

            if(error?.data?.message) {
                reject({error:error.data.message});
            }

            console.log(`brr ${error.data}`)
        });
    });
}

export { Login, freshData, UpdateEmail };
