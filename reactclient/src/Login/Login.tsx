import * as React from 'react';
import { Card, Typography, TextField, Button, Grid } from '@material-ui/core';
import { authService } from '../Auth/AuthService';

export function Login(): React.ReactElement {
	const [twitterHandle, setTwitterHandle] = React.useState('');

	const handleSubmit = () => {
		authService.login(twitterHandle);
	};

	return (
		<Grid container justify="center">
			<Grid item xs={10} sm={6}>
				<Spacer height={50}/>
				<Card style={{ flexDirection: 'column', display: 'flex' }}>
					<Grid container justify="center">
						<Grid item xl={9} style={{ flexDirection: 'column', display: 'flex' }}>
							<Typography variant="h6" component="h3" style={{textAlign: 'center'}}>Login</Typography>
							<Spacer height={20}/>
							<TextField
								label="Twitter Handle"
								value={twitterHandle}
								onChange={(e) => { setTwitterHandle(e.target.value) }}
								onSubmit={handleSubmit}
							/>
							<Spacer height={20}/>
							<Button onSubmit={handleSubmit}>Login</Button>
							<Spacer height={20}/>
						</Grid>
					</Grid>
				</Card>
			</Grid>
		</Grid>
	);
}

const Spacer = ({height}: {height: number}) => <div style={{height: height}}/>