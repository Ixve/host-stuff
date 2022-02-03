import { useRouter } from 'next/router';
import { LayoutContainer } from '../layout/LayoutContainer';
import {Typography} from '@material-ui/core'

interface Props {
    /**
     * Injected by the documentation to work in an iframe.
     * You won't need it on your project.
     */
    window?: () => Window;
};

export const DashboardContainer: React.FC<Props>  = (props) => {
    const router = useRouter()
    return (
        <LayoutContainer title="Dashboard">
            <Typography color="white" variant="h1">{router.pathname}</Typography>
        </LayoutContainer>
    );
};