import * as React from 'react';
import { useStoreState } from 'easy-peasy';
import { useRouter } from 'next/router';
import * as API from '../api';
import { AccountContainer } from '../components/account/AccountContainer';

export default function Account() {
    const user = useStoreState(state => state.user?.data);
    const router = useRouter();

    React.useEffect(() => {
        if (!user) {
            router.push('/');
        } 
        API.freshData().catch(error => router.push('/'))
    }, []);

    return (
        <AccountContainer />
    );
}