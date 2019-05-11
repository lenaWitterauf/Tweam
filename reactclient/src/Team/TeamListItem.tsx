import * as React from 'react';
import { Card, CardContent, Typography, ListItem, List } from '@material-ui/core';
import { TeamInterface } from '../interfaces/Team.interface';
import { User } from '../User/User';

export function TeamListItem(team: TeamInterface) {
	return (
		<Card>
			<CardContent>
				<Typography variant="h5" component="h5">{team.name}</Typography>
				<Typography>Members:</Typography>
				<List>
					{team.members.map(user => (
						<ListItem disableGutters key={`user-${user.id}`}>
							<User {...user}/>
						</ListItem>
					))}
				</List>
			</CardContent>
		</Card>
	);
}