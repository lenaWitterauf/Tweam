import { Button, Card, Chip, TextField, Typography } from '@material-ui/core';
import * as React from 'react';
import { RouterProps, withRouter } from 'react-router';
import { networkService } from '../Network/NetworkService';

export const Register = withRouter(RegisterBase);

export function RegisterBase(props: RouterProps) {
	const [handle, setHandle] = React.useState('');
	const [keywords, setKeywords] = React.useState<string[]>([]);
	const [text, setText] = React.useState<string>('');

	const handleSubmit = async () => {
		const createdUser = await networkService.createUser({twitterHandle: handle, tokens: keywords});
		if (createdUser) {
			props.history.push(`/user/${createdUser.twitterHandle}`);
		}
	};

	return (
		<Card>
			<Typography variant="h3" component="h3">Registration</Typography>
			<SubmitInput
				label="Twitter-Handle"
				setText={setHandle}
				onSubmit={handleSubmit}
				text={handle}
			/>
			<Typography variant="body1">Keywords</Typography>
			<div className="keywords">
				{keywords.map((abc) => (<Chip
					label={abc}
					onDelete={(event) => setKeywords(keywords.filter((e) => e !== abc))}
				/>))}
			</div>
			<SubmitInput
				onSubmit={() => {
					const newKeywords: string[] = [...keywords];
					newKeywords.push(text);
					setKeywords(newKeywords);
					setText("");
				}}
				text={text}
				setText={setText}
				label="Keywords"
			/>
			<Button variant="contained" color="primary" onClick={handleSubmit}>Send</Button>
	</Card>
	);
}

function SubmitInput(props: {label: string, text: string, setText: (text: string) => void, onSubmit: () => void | Promise<void>}) {
	return <form
		noValidate
		autoComplete="off"
		onSubmit={(e) => {
			e.preventDefault();
			props.onSubmit();
		}}
	>
		<TextField
			label={props.label}
			value={props.text}
			onChange={(event => props.setText(event.target.value))}
			margin="normal"
			variant="outlined"
			onSubmit={props.onSubmit}
		/>
	</form>;
}