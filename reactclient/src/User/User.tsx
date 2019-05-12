import * as React from 'react';
import { UserInterface } from '../interfaces/User.interface';
import { Typography, Link, Card, CardContent } from '@material-ui/core';
import './User.scss';
import TweetEmbed from "react-tweet-embed/dist/tweet-embed";

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
		<Link href={profileUrl} classes={linkClasses} target="_blank">
			<Card>
				<CardContent classes={cardClasses}>
					<img src={user.profilePicUrl} alt="profile pic" />
					<Typography>{user.name}</Typography>
				</CardContent>
				<TweetEmbed id={user.statusId} placeholder={'loading'}/>
			</Card>
		</Link>
	);
}