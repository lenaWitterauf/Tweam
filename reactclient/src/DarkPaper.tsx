import * as React from 'react';
import { Paper } from '@material-ui/core';

export function DarkPaper({children}: React.PropsWithChildren<{}>) {
	return <Paper style={{background: '#777'}}>{children}</Paper>;
}