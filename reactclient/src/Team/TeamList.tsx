import { List, ListItem } from '@material-ui/core';
import * as React from 'react';
import { TeamInterface } from '../interfaces/Team.interface';
import { networkService } from '../Network/NetworkService';
import { TeamListItem } from './TeamListItem';

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
		});
	}, [false]);

	return (
		<List>
			<div>
				{teams.map((team, index) => (
					<ListItem>
						<TeamListItem {...team}/>
					</ListItem>
				))}
			</div>
		</List>
	);
}