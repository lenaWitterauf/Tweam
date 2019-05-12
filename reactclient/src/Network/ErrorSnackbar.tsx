import * as React from 'react';
import { SnackbarContent, Snackbar } from '@material-ui/core';

export function ErrorSnackbar(props: {error: string, open: boolean, onClose(): void }) {
	console.log('foo')
	return (
		<Snackbar
			anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
			open={props.open}
			autoHideDuration={2000}
			onClose={props.onClose}
			message={props.error}
		/>
	);
}