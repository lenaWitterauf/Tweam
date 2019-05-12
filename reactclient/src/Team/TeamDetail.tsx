import { List, ListItem } from '@material-ui/core';
import * as React from 'react';
import { TeamInterface } from '../interfaces/Team.interface';
import { networkService } from '../Network/NetworkService';
import './Team.scss';
import { TeamListItem } from './TeamListItem';
import { DarkPaper } from '../DarkPaper';
import {UserInterface} from "../interfaces/User.interface";
import {RouterProps} from "react-router";

export function TeamDetail( props: RouterProps) {
	const [team, setTeam] = React.useState<TeamInterface>();

	React.useEffect(() => {
		const path = props.history.location.pathname.split("/");
		networkService.getUserByHandle(path[path.length-1]).then((user) => {
			if (user) {
				networkService.getTeamForUser(user).then((team) => {
					if(team) {
						setTeam(team);
					}
				});
			}

		});
	}, [false]);



	return team? (
		<DarkPaper>
			<TeamListItem {...team}/>
		</DarkPaper>
	) : null;
}