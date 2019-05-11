import * as React from 'react';
import { List, ListItem } from '@material-ui/core';
import { TeamListItem } from './TeamListItem';
import { TeamInterface } from '../interfaces/Team.interface';

export interface TeamListProps {
	teams: TeamInterface[];
}

export function TeamList(props: TeamListProps) {
	return (
		<List>
			{props.teams.map(team => (
				<ListItem key={`team-${team.id}`}>
					<TeamListItem {...team}/>
				</ListItem>
			))}
		</List>
	);
}