import { LayoutContainer } from '../layout/LayoutContainer';

import React from "react";
import { Typography } from "@material-ui/core";
import EmailBox from './EmailBox';

interface Props {
    /**
     * Injected by the documentation to work in an iframe.
     * You won't need it on your project.
     */
    window?: () => Window;
}

export const AccountContainer: React.FC<Props>  = (props) => {
    return (
        <LayoutContainer title="Account">
            <EmailBox></EmailBox>
        </LayoutContainer>
    );
};
