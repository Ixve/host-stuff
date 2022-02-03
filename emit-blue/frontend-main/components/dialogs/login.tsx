import { 
  Button, 
  Dialog, 
  DialogActions, 
  DialogContent, 
  DialogContentText, 
  DialogTitle, 
  TextField,
  DialogProps,
  Snackbar 
} from '@material-ui/core';
import MuiAlert, { AlertProps } from '@material-ui/core/Alert';
import * as React from 'react';
import * as API from '../../api';
import { useStoreState, useStoreActions } from 'easy-peasy';
import { useRouter } from 'next/router';

type props = Omit<DialogProps,'children'>;

interface finProps extends props {
  closeModalState:(() => void);
};

const Alert = React.forwardRef<HTMLDivElement, AlertProps>(function Alert(
  props,
  ref,
) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function LoginDialog(Props: finProps) {
  const user = useStoreState(state => state.user?.data);
  const router = useRouter();
  const [ Form, setForm ] = React.useState({
    password: '',
    username: '',
  });
  const [Loading,setLoading] = React.useState(false)
  const [error,setError] = React.useState("")
  function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setLoading(true)
    API.Login(Form.username, Form.password).then(response => {
      setLoading(false)
      router.push('/dashboard');
    }).catch((err) => {
      setLoading(false)
       if(err?.response) {
        err = err.response
          if(!err?.data) {
          setError("An unknown error occured")
           setOpen(true)
           setTimeout(() => {
              setOpen(false)
            },2000)
          return
        }
        if(err?.data?.message) {
          setError(err?.data?.message)
           setOpen(true)
           setTimeout(() => {
              setOpen(false)
            },2000)
          return
        }
      } else {
        setError("An unknown error occured")
        setOpen(true)
        setTimeout(() => {
           setOpen(false)
         },2000)
        return
      }

    });
  }
  const [open, setOpen] = React.useState(false);

  const handleClose = (event?: React.SyntheticEvent, reason?: string) => {
    if (reason === 'clickaway') {
      return;
    }

    setOpen(false);
  };


  return (
    <Dialog open={Props.open} onClose={Props.onClose} sx={{ background: '#121212', padding: '1em' }}>
      <form onSubmit={onSubmit}>

      <DialogTitle color='white' sx={{ background: '#121212'}}>Login</DialogTitle>
      <DialogContent sx={{ background: '#121212', color: 'white' }}>

        <DialogContentText sx={{ background: '#121212', color: 'white' }}>
          Please enter your login credentials.
        </DialogContentText>
          <TextField
            autoFocus
            margin='dense'
            label='Username'
            type='text'
            fullWidth
            variant='standard'
            onChange={(e) => setForm({...Form,username:e.target.value})}
            value={Form.username}
            color='primary'
            focused
            InputProps={{ style: { color: 'white' } }}
            disabled={Loading}
          />
          <TextField
            margin='dense'
            label='Password'
            type='password'
            fullWidth
            variant='standard'
            onChange={(e) => setForm({...Form,password:e.target.value})}
            value={Form.password}
            focused
            InputProps={{ style: { color: 'white' } }}
            disabled={Loading}
          />
        </DialogContent>
        <DialogActions sx={{ background: '#121212', paddingBottom: '1em', paddingRight: '23px' }}>
          <Button onClick={Props.closeModalState} variant='outlined' disabled={Loading} >Cancel</Button>
          <Button type='submit' variant='outlined' disabled={Loading} >Login</Button>
        </DialogActions>
      </form>
                 <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                 <Alert onClose={handleClose} severity="error" sx={{ width: '100%' }}>
                   {error}
                 </Alert>
               </Snackbar>
    </Dialog>
  );
}