import { Button, Chip, TextField, Typography } from '@material-ui/core';
import * as React from 'react';
import { RouterProps, withRouter } from 'react-router';
import { CenterCard } from '../CenterCard';
import { networkService } from '../Network/NetworkService';
import { VerticalSpacer } from '../Spacer';
import './Register.scss';

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
		<CenterCard>
			<VerticalSpacer height={20}/>
			<Typography variant="h4" component="h3" style={{textAlign: 'center'}}>Registration</Typography>
			<SubmitInput
				label="Twitter Handle"
				setText={setHandle}
				onSubmit={handleSubmit}
				text={handle}
			/>
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
			<div className="keywords">
				{keywords.map((abc) => (<Chip
					label={abc}
					onDelete={(event) => setKeywords(keywords.filter((e) => e !== abc))}
				/>))}
			</div>
			<VerticalSpacer height={keywords.length ? 0 : 20}/>
			<Button variant="contained" color="primary" onClick={handleSubmit} disabled={!handle}>Send</Button>
			<VerticalSpacer height={20}/>
	</CenterCard>
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
			style={{width: '100%'}}
		/>
	</form>;
}