import * as React from 'react';
import { RouterProps } from 'react-router';
import { UserInterface } from '../interfaces/User.interface';
import { networkService } from '../Network/NetworkService';
import { User } from './User';

export function UserFetcher( props: RouterProps) {
    const [user, setUser] = React.useState<UserInterface>();

	React.useEffect(() => {
        const path = props.history.location.pathname.split("/");
		networkService.getUserByHandle(path[path.length-1]).then((user) => {
            if(user){
                setUser(user);
            }
        });
	}, [false]);
    return user?(
        <User {...user}/>
    ):null;
}