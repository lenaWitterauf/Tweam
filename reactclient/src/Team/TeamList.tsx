import { List, ListItem } from '@material-ui/core';
import * as React from 'react';
import { TeamInterface } from '../interfaces/Team.interface';
import { networkService } from '../Network/NetworkService';
import './Team.scss';
import { TeamListItem } from './TeamListItem';
import { DarkPaper } from '../DarkPaper';

export function TeamList() {
	const [teams, setTeams] = React.useState<TeamInterface[]>([]);

	React.useEffect(() => {
		networkService.getAllTeams().then(newTeams => {
			if (newTeams) {
				setTeams(newTeams as any);
			}
		});
	}, [false]);

	return (
		<DarkPaper>
			<List>
				{teams.map((team, index) => (
					<ListItem classes={{ root: 'ListItem' }}>
						<TeamListItem {...team}/>
					</ListItem>
				))}
			</List>
		</DarkPaper>
	);
}