import { Button, Chip, TextField, Typography } from '@material-ui/core';
import * as React from 'react';
import { RouterProps, withRouter } from 'react-router';
import { UserInterface } from '../interfaces/User.interface';

export const Register = withRouter(RegisterBase);

export function RegisterBase(props: RouterProps) {
	const [handle, setHandle] = React.useState('');
	const [keywords, setKeywords] = React.useState<string[]>([]);
	const [text, setText] = React.useState<string>('');

	return (
		<>
			<Typography variant="h3" component="h3">Registration</Typography>
			<form
				noValidate
				autoComplete="off"
				onSubmit={async (event) => {
					event.preventDefault();
					try {
						const resp = await fetch("http://localhost:8080/user/create", {
							method: "POST",
							body: JSON.stringify({
								handle: handle,
								keywords: keywords,
							}),
							headers: {
								"content-type": "application/Json"
							}
						})
						if (resp.ok) {
							const user: UserInterface = await resp.json();
							props.history.push(`/user/${user.twitterHandle}`);

						} else {
							console.error("Endpoint error")
						}
					} catch (e) {
						console.error(e);
					}

				}}
			>
				<TextField
					id="outlined-name"
					label="Twitter-Handle"
					helperText="Add your Twitter-Handle to create a Account."
					onChange={(event) => setHandle(event.target.value)}
					margin="normal"
					variant="outlined"
				/>
			</form>
			<Typography variant="body1">Keywords</Typography>
			<div className="keywords">
				{keywords.map((abc) => (<Chip
					label={abc}
					onDelete={(event) => setKeywords(keywords.filter((e) => e !== abc))}
				/>))}
			</div>
			<form
				noValidate
				autoComplete="off"
				onSubmit={(event) => {
					event.preventDefault();
					const newKeywords: string[] = [...keywords];
					newKeywords.push(text);
					setKeywords(newKeywords);
					setText("");
				}
				}
			>

				<TextField
					id="outlined-keywords"
					label="Keywords"
					helperText="Add some Keywords to describe your interessants."
					value={text}
					onChange={(event => setText(event.target.value))}
					margin="normal"
					variant="outlined"
				/>

			</form>
			<Button variant="contained" color="primary" onClick={async (event) => {
				event.preventDefault();
				try {
					const resp = await fetch("http://localhost:8080/user/create", {
						method: "POST",
						body: JSON.stringify({
							handle: handle,
							keywords: keywords,
						}),
						headers: {
							"content-type": "application/Json"
						}
					})
					if (resp.ok) {
						const user: UserInterface = await resp.json();
						props.history.push(`/user/${user.twitterHandle}`);

					} else {
						console.error("Endpoint error")
					}
				} catch (e) {
					console.error(e);
				}

			}}>
				Send
      </Button>
		</>
	);
}