import { List, ListItem } from '@material-ui/core';
import * as React from 'react';
import { TeamInterface } from '../interfaces/Team.interface';
import { networkService } from '../Network/NetworkService';
import { TeamListItem } from './TeamListItem';
import { CarouselProvider, Slider, Slide } from 'pure-react-carousel';

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
		<CarouselProvider totalSlides={teams.length} naturalSlideHeight={1} naturalSlideWidth={1}>
			<Slider>
				{teams.map((team, index) => (
					<Slide index={index} key={`team-${team.id}`}>
						<div style={{ display: 'flex', justifyContent: 'center' }}>
							<TeamListItem {...team}/>
						</div>
					</Slide>
				))}
			</Slider>
		</CarouselProvider>
	);
}