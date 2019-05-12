import * as React from 'react';
import { Grid, Card } from '@material-ui/core';
import { VerticalSpacer } from './Spacer';

export function CenterCard({children}: React.PropsWithChildren<{}>) {
	return (
		<Grid container justify="center">
			<Grid item xs={10} sm={6}>
				<VerticalSpacer height={50}/>
				<Card style={{ flexDirection: 'column', display: 'flex' }}>
					<Grid container justify="center">
						<Grid item xl={9} style={{ flexDirection: 'column', display: 'flex' }}>
							{children}
						</Grid>
					</Grid>
				</Card>
			</Grid>
		</Grid>
	);
}