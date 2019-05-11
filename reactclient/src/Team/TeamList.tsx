import * as React from 'react';
import { List, ListItem, Button } from '@material-ui/core';
import { TeamListItem } from './TeamListItem';
import { TeamInterface } from '../interfaces/Team.interface';
import { networkService } from '../Network/NetworkService';

export interface TeamListProps {
	teams: TeamInterface[];
}

export function TeamList(props: TeamListProps) {
	const [teams, setTeams] = React.useState<TeamInterface[]>([]);

	React.useEffect(() => {
		networkService.getAllTeams().then(newTeams => {
			if (newTeams) {
				setTeams(newTeams as any);
			}
		})
	}, [false]);

	return (
		<List>
			{teams.map(team => (
				<ListItem key={`team-${team.id}`}>
					<TeamListItem {...team}/>
				</ListItem>
			))}
		</List>
	);
}