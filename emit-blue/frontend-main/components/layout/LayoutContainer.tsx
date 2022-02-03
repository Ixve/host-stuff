import * as React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Box from '@material-ui/core/Box';
import CssBaseline from '@material-ui/core/CssBaseline';
import Divider from '@material-ui/core/Divider';
import Drawer from '@material-ui/core/Drawer';
import IconButton from '@material-ui/core/IconButton';
import InboxIcon from '@material-ui/icons/MoveToInbox';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import MailIcon from '@material-ui/icons/Mail';
import MenuIcon from '@material-ui/icons/Menu';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import NavItem from './NavItem';
import { useRouter } from 'next/router';
import DashboardIcon from '@material-ui/icons/Dashboard';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import { Avatar, Menu, MenuItem } from '@material-ui/core';
import { useStoreState } from 'easy-peasy';
const drawerWidth = 240;

interface Props {
  /**
   * Injected by the documentation to work in an iframe.
   * You won't need it on your project.
   */
  window?: () => Window;
  title: string;
}

interface INavItem {
  icon:React.ReactElement<any, any>
  active:boolean
  text:string
  page:string
}

 

export const LayoutContainer : React.FC<Props>  = (props) => {
  const { window } = props;
  const [mobileOpen, setMobileOpen] = React.useState(false);
  const router = useRouter()
  const NavEntries1 : Array<INavItem> = [
    {
     text:"Dashboard",
     active:(router.pathname == "/dashboard") ? true : false,
     icon: <DashboardIcon />,
     page: "/dashboard"
    },
    {
      text:"Account",
      active:(router.pathname == "/account") ? true : false,
      icon: <AccountCircleIcon />,
      page: "/account"
     }
   ]

   console.log(NavEntries1)
   const user = useStoreState(state => state.user?.data);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const container = window !== undefined ? () => window().document.body : undefined;

  const drawer = (
    <div style={{ background:"#000000",overflow:"hidden" }}>
      <Toolbar>
        <Typography variant="h5" fontWeight="bold" color="white">emit.blue</Typography>
      </Toolbar>
      <Divider sx={{ background:"#54626F", mx:"0.5em" }} />
      <List>
        {NavEntries1.map((entry, index) => (
          <NavItem text={entry.text} icon={entry.icon} active={entry.active} page={entry.page} key={index}/>
        ))}
      </List>
      <Divider sx={{ background: "#54626F", mx: "0.5em" }} />
      <List>
        {['Changelogs', 'Domains'].map((text, index) => (
          <ListItem button key={text}>
            <ListItemIcon sx={{ color:"white" }}>
              {index % 2 === 0 ? <InboxIcon /> : <MailIcon />}
            </ListItemIcon>
            <ListItemText primary={text} sx={{ color:"white" }} />
          </ListItem>
        ))}
      </List>
    </div>
  );



  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);
  const handleClickProfile = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <Box sx={{ display: "flex" }}>
      <CssBaseline />
      <AppBar
        position="fixed"
        sx={{
          width: { sm: `calc(100% - ${drawerWidth}px)` },
          ml: { sm: `${drawerWidth}px` },
          backgroundColor: "#1f2120",
          paddingRight:"0px"
        }}
        className="bruh"
        elevation={0}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            sx={{ mr: 2, display: { sm: 'none' } }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap component="div" marginTop="2px" sx={{flexGrow:1}}>
            { props.title }
          </Typography>
          {/*
 // @ts-ignore */}
          <Avatar 
            onClick={handleClickProfile}
          >
            {user?.username.substr(0,1)}
          </Avatar>
          <Menu
        id="basic-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        MenuListProps={{
          'aria-labelledby': 'basic-button',
        }}
        
      >
        <MenuItem onClick={handleClose}>Profile</MenuItem>
        <MenuItem onClick={handleClose}>My account</MenuItem>
        <MenuItem onClick={handleClose}>Logout</MenuItem>
      </Menu>
        </Toolbar>
      </AppBar>
      <Box
        component="nav"
        sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
        aria-label="mailbox folders"
      >
        <Drawer
          container={container}
          variant="temporary"
          open={mobileOpen}
          onClose={handleDrawerToggle}
          ModalProps={{
            keepMounted: true, 
          }}
          sx={{
            display: { xs: "block", sm: "none" },
            '& .MuiDrawer-paper': { boxSizing: "border-box", width: drawerWidth,backgroundColor: "black" },
          }}
          color="#000000"
        >
          {drawer}
        </Drawer>
        <Drawer
          variant="permanent"
          sx={{
            display: { xs: "none", sm: "block" },
            '& .MuiDrawer-paper': { boxSizing: "border-box", width: drawerWidth,backgroundColor: "black" },
          }}
          open
        >
          {drawer}
        </Drawer>
      </Box>
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        { props.children }
      </Box>
    </Box>
  );
}
