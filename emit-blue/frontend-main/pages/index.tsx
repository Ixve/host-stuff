import * as React from 'react';
import { useRouter } from 'next/router';
import { useStoreState } from 'easy-peasy';
import {
  Box,
  Button,
  ButtonGroup,
  Card,
  CardContent,
  Grid,
  Typography,
} from "@material-ui/core";
import PeopleIcon from "@material-ui/icons/People";
import LoginDialog from "../components/dialogs/login";

export default function Index() {
  const initialState = {
    showLoginModal: false,
    showRegisterModal: false,
  };

  const [{ showLoginModal, showRegisterModal }, setState ] = React.useState(initialState);

  const user = useStoreState(state => state.user?.data);
  const router = useRouter();

  React.useEffect(() => {
      if (user) {
          router.push('/dashboard');
      }
  }, []);;

  function setLoginModalState(state: boolean) {
    setState({
      showLoginModal: state,
      showRegisterModal,
    });
  }

  return (
    <div>
      <Box
        sx={{
          height: "100vh",
          background: "#121212",
          display: "flex",
          justifyContent: "center",
          alignContent: "center",
        }}
      >
        <Box sx={{ margin: "auto" }}>
          <Typography
            color="white"
            variant="h2"
            fontWeight="bold"
            align="center"
          >
            emit.blue
          </Typography>
          <Box
            sx={{
              marginTop: "1em",
              display: "flex",
              justifyContent: "center",
              marginBottom: "1em",
            }}
          >
            <Button sx={{ mx: "0.5em" }} variant="outlined" color="primary">
              Register
            </Button>
            <Button sx={{ mx: "0.5em" }} variant="outlined" color="primary"
              onClick={() => setLoginModalState(true)}
            >
              Login
            </Button>
            <Button sx={{ mx: "0.5em" }} variant="outlined" color="primary">
              Discord
            </Button>
          </Box>
          <Box
            sx={{
              marginTop: "1em",
              display: "flex",
              justifyContent: "center",
              marginBottom: "1em",
            }}
          >
            <Typography sx={{ mx: "0.7em" }} color="GrayText" align="center">
              Terms of Service
            </Typography>
            <Typography sx={{ mx: "0.7em" }} color="GrayText" align="center">
              Privacy Policy
            </Typography>
          </Box>
          <LoginDialog
            open={showLoginModal}
            onClose={() => setLoginModalState(false)}
            closeModalState={() => setLoginModalState(false)}
          />
        </Box>
      </Box>
    </div>
  );
}