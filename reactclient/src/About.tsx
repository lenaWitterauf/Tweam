import * as React from 'react';
import { CenterCard } from './CenterCard';
import { VerticalSpacer } from './Spacer';
import { Typography } from '@material-ui/core';
import { DarkPaper } from './DarkPaper';

export function About() {
	console.log('👣')
	return (
		<DarkPaper>
			<CenterCard>
				<VerticalSpacer height={20}/>
				<Typography variant="h4" component="h3" style={{textAlign: 'center'}}>Tweam</Typography>
				<VerticalSpacer height={10}/>
				<Typography style={{padding: 10, textAlign: 'center'}}>
					As we all know conferences and other MeetUps, such as today's Hackathon, are awesome. One challenge that often comes up before these events is finding people with similar interests you can collaborate and network with.
				</Typography>
				<VerticalSpacer height={20}/>
				<Typography variant="h4" component="h3" style={{textAlign: 'center'}}>What does it do?</Typography>
				<VerticalSpacer height={10}/>
				<Typography style={{padding: 10, textAlign: 'center'}}>
					Tweam analyzes how well people fit based on their Twitter activity. A smart algorithm matches participants by shared interests extracted from the Twitter API. A straightforward UI offers registration only by your Twitter handle. You can search through existing teams, and see a simple and clean overview of your match. With Tweam you can find a group of people to participate in a city clean up, as well as contacts to expand your network during the next cool tech conference. Also, events benefit from Tweam as increased Twitter activity leads to increased public attention for the event.
				</Typography>
				<VerticalSpacer height={20}/>
			</CenterCard>
		</DarkPaper>
	);
}