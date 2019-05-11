import * as React from 'react';
import { UserInterface } from '../interfaces/User.interface';
import { Typography, Link, Card, CardContent } from '@material-ui/core';
import './User.scss';

const twitterProfileBaseUrl = 'https://twitter.com/';
const cardClasses = {
	root: 'user'
};
const linkClasses = {
	root: 'fullWidth'
}

export function User(user: UserInterface) {
	const profileUrl = `${twitterProfileBaseUrl}${user.twitterHandle}`;
	return (
		<Link href={profileUrl} classes={linkClasses}>
			<Card>
				<CardContent classes={cardClasses}>
					<img src={user.profilePicUrl} alt="profile pic" />
					<Typography>{user.name}</Typography>
				</CardContent>
			</Card>
		</Link>
	);
}