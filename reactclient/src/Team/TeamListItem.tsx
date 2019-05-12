import { Card, CardContent, Grid, Typography } from '@material-ui/core';
import * as React from 'react';
import { TeamInterface } from '../interfaces/Team.interface';
import { User } from '../User/User';

export function TeamListItem(team: TeamInterface) {
	return (
		<Card style={{width: '100%'}}>
			<CardContent>
				<Typography variant="h5" component="h5">Team {team.id}</Typography>
				<Typography>Members:</Typography>
				<Grid container spacing={8}>
					{team.members.map(user => (
						<Grid item xs={12} sm={4} md={4} key={`user-${user.id}`}>
							<User {...user}/>
						</Grid>
					))}
				</Grid>
			</CardContent>
		</Card>
	);
}