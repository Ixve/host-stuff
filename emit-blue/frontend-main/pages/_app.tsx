import { ThemeProvider } from '@material-ui/core';
import { StoreProvider, useStoreState } from 'easy-peasy'; 
import type { AppProps } from 'next/app';
import '../styles/global.css';
import { Store } from '../state';
import Theme from '../components/theme';
import React from 'react';
import * as API from '../api';

function App({ Component, pageProps }: AppProps) {
//   const user = useStoreState(state => state?.user?.data);
//   React.useEffect(() => {
//     if(user) {
//       API.freshData().catch()
//     }
//  }, [Component]);
  return ( 
  <ThemeProvider theme={Theme}>
    <StoreProvider store={Store}>
      <Component {...pageProps} />
    </StoreProvider>
  </ThemeProvider>
  )
}

export default App;