import * as React from 'react';
import Box from '@material-ui/core/Box';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import { TextField, Snackbar } from '@material-ui/core';
import MuiAlert, { AlertProps } from '@material-ui/core/Alert';
import * as API from '../../api';

const Alert = React.forwardRef<HTMLDivElement, AlertProps>(function Alert(
  props,
  ref,
) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function EmailBox() {
  const [error, setError] = React.useState('');
  const [message, setMessage] = React.useState('');
  const [openError, setOpenError] = React.useState(false);
  const [openSuccess, setOpenSuccess] = React.useState(false);
  
  const [ form,setForm ] = React.useState({
    password: '',
    email: '',
    confirm_email: '',
    loading: false
  });
  
  const handleErrorClose = (event?: React.SyntheticEvent, reason?: string) => {
    if (reason === 'clickaway') {
      return;
    }

    setOpenError(false);
  };

  const handleSuccessClose = (event?: React.SyntheticEvent, reason?: string) => {
    if (reason === 'clickaway') {
      return;
    }

    setOpenSuccess(false);
  };

  function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    setForm({
      ...form,
      loading: true,
    });

    return API.UpdateEmail(form.password, form.email, form.confirm_email).then(response => {
      console.log(response);
      setOpenSuccess(true);
      setMessage(response.message);

      setTimeout(() => {
        setOpenSuccess(false);
      }, 2000);

      setForm({
        ...form,
        loading: false
      });
    }).catch(err => {
      console.log(err)
    });
  }
  
  return (
    <>
    <Card sx={{ minWidth: 275, background: "#1f2120" }}>
      <form onSubmit={handleSubmit}>
        <CardContent>
          <Typography variant="h5" component="div" color="white" fontWeight="bold" marginBottom="1em">
            Change email
          </Typography>
          <div>
            <TextField 
              type="password"
              focused
              required
              label="Enter your current password"
              variant="outlined"
              sx={{ width: "100%" }}
              InputProps={{ style: { color: "white", marginBottom: "0.9em", width:"100%" } }}
              onChange={((e) => setForm({...form, password: e.target.value}))}
              disabled={form.loading}
            />
          </div>
          <div>
            <TextField 
              type="email"
              focused
              required
              label="Enter your new email"
              variant="outlined"
              sx={{ width:"100%" }}
              InputProps={{ style: { color: "white", marginBottom: "0.9em", width:"100%" } }}
              onChange={((e) => setForm({...form, email: e.target.value}))}
              disabled={form.loading}
            />
          </div>
          <div>
            <TextField 
              type="email"
              focused
              required
              label="Confirm your new email"
              variant="outlined"
              sx={{ width: "100%" }}
              InputProps={{ style: { color: "white", marginBottom: "0.1em", width: "100%" } }}
              onChange={((e) => setForm({...form, confirm_email: e.target.value}))}
              disabled={form.loading}
            />
          </div>
        </CardContent>
        <CardActions sx={{ marginLeft: "8px", marginBottom: "10px" }}>
          <Button size="medium" variant="contained" type="submit">Submit</Button>
        </CardActions>
      </form>
    </Card>
    <Snackbar open={openError} autoHideDuration={6000} onClose={handleErrorClose}>
      <Alert onClose={handleErrorClose} severity="error" sx={{ width: '100%' }}>
        {error}
      </Alert>
    </Snackbar>
    <Snackbar open={openSuccess} autoHideDuration={6000} onClose={handleSuccessClose}>
      <Alert onClose={handleSuccessClose} severity="success" sx={{ width: '100%' }}>
        {message}
      </Alert>
    </Snackbar>
  </>
  );
}