import * as React from 'react';
import { useStoreState } from 'easy-peasy';
import { useRouter } from 'next/router';
import * as API from '../api';
import { DashboardContainer } from '../components/dashboard/DashboardContainer';
import { Typography, Button } from '@material-ui/core';
export default function Dashboard() {
    const user = useStoreState(state => state.user?.data);
    const router = useRouter();

    React.useEffect(() => {
        if (!user) {
            router.push('/');
        }
        API.freshData().catch(error => router.push('/'))
    }, []);
    return (
        <DashboardContainer>
            <Typography color="white">
                {router.pathname}
            </Typography>
        </DashboardContainer>
    );
}
