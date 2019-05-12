import { Button, TextField, Typography } from '@material-ui/core';
import * as React from 'react';
import { authService } from '../Auth/AuthService';
import { CenterCard } from '../CenterCard';
import { VerticalSpacer } from '../Spacer';

export function Login(): React.ReactElement {
	const [twitterHandle, setTwitterHandle] = React.useState('');

	const handleSubmit = () => {
		if (twitterHandle) {
			authService.login(twitterHandle);
		}
	};

	return (
		<CenterCard>
			<VerticalSpacer height={20}/>
			<Typography variant="h4" component="h3" style={{textAlign: 'center'}}>Login</Typography>
			<VerticalSpacer height={16}/>
			<TextField
				label="Twitter Handle"
				value={twitterHandle}
				onChange={(e) => { setTwitterHandle(e.target.value) }}
				onSubmit={handleSubmit}
				variant="outlined"
			/>
			<VerticalSpacer height={20}/>
			<Button variant="contained" color="primary" onClick={handleSubmit} disabled={!twitterHandle}>Login</Button>
			<VerticalSpacer height={20}/>
		</CenterCard>
	);
}