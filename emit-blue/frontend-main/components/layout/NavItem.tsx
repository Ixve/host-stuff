import { ListItem,ListItemIcon,ListItemText, SvgIconTypeMap } from "@material-ui/core";
import { OverridableComponent } from "@material-ui/core/OverridableComponent";
import { ReactElement } from "react";
import Link from 'next/link'

interface props {
    icon:ReactElement<any, any>
    active:boolean
    text:string
    page:string
}

export default function NavItem(Props : props) {

    return (
        <Link href={Props.page} passHref>
        <ListItem 
        button 
        className={Props.active ? "active" : ""}
        sx={{ml:"0.5em"}}
        >
            <ListItemIcon sx={{color:"white"}} >
              {Props.icon}
            </ListItemIcon>
            <ListItemText primary={Props.text} sx={{color:"white"}} />
          </ListItem>
        </Link>     
    )
}