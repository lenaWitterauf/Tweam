import * as React from 'react';
import TextField from '@material-ui/core/TextField';
import { UserInterface } from '../interfaces/User.interface';
import { withRouter, RouterProps } from 'react-router';
export const Register = withRouter(RegisterBase);

function RegisterBase(props: RouterProps) {
    const [handle, setHandle] = React.useState("");
	return (
        <form
            noValidate
            autoComplete="off"
            onSubmit={async(event) => {
                event.preventDefault(); 
                try{
                    const resp = await fetch("http://localhost:8080/user/create", {
                        method: "POST", 
                        body: JSON.stringify({
                            handle: handle, 
                            keywords : ["Hallo", "Schokolade"],
                        }),
                        headers: {
                            "content-type": "application/Json"
                        }
                })
                if(resp.ok){
                    const user:UserInterface = await resp.json();
                    props.history.push(`/user/${user.twitterHandle}`);
                    
                }else{
                    console.error("Endpoint error")
                }
                }catch(e){
                    console.error(e);
                }
                
            }}
        >
            <TextField
                id="outlined-name"
                label="Name"
                onChange={(event) => setHandle(event.target.value)}
                margin="normal"
                variant="outlined"
            />
             <TextField
                id="outlined-name"
                label="Name"
                onChange={(event) => setHandle(event.target.value)}
                margin="normal"
                variant="outlined"
            />
        </form>
	);
}