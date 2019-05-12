import { AppBar, Link, MuiThemeProvider, Paper, Toolbar, Button } from '@material-ui/core';
import React from 'react';
import { BrowserRouter as Router, Link as RouteLink, Route, Switch } from 'react-router-dom';
import { authService } from './Auth/AuthService';
import { Login } from './Login/Login';
import { Register } from './Register/Register';
import { TeamList } from './Team/TeamList';
import { darkTheme } from './themes/dark';
import { UserFetcher } from './User/UserFetcher';
import { About } from './About';
import { errorService } from './ErrorService';
import { ErrorSnackbar } from './Network/ErrorSnackbar';
import {TeamDetail} from "./Team/TeamDetail";

const NavLink = (props: { href?: string }): React.ReactElement => <RouteLink to={props.href || ''} {...props} style={{paddingRight: 5, paddingLeft: 5}} />

export class App extends React.PureComponent<{}, { isLoggedIn: boolean, error: string | undefined }> {
	state = { isLoggedIn: false, error: undefined };
	private snackbarTimeout: any = undefined;

	componentDidMount() {
		authService.registerListener(this);
		errorService.registerListener(this.handleError);
	}

	componentWillUnmount() {
		authService.unregisterListener(this);
	}

	readonly onAuthChange = (isLoggedIn: boolean) => {
		this.setState({ isLoggedIn });
	};

	readonly handleError = (error: string) => {
		this.setState({ error });
	};

	render() {
		return (
			<Router>
				<MuiThemeProvider theme={darkTheme}>
					<AppBar position="relative">
						<Toolbar>
							<Link variant="h5" component={NavLink} href="/home" color="inherit">Tweam</Link>
							<div style={{ flexGrow: 1 }} />
							{this.state.isLoggedIn ? this.renderLoggedInMenu() : this.renderLoggedOutMenu()}
						</Toolbar>
					</AppBar>

					<Switch>
						<Route path="/teams" component={TeamList} />
						<Route path="/user/:twitterHandle" component={UserFetcher} />
						<Route path="/team/:teamId" component={TeamDetail} />
						{!this.state.isLoggedIn && this.renderLoggedOutRoutes()}
						<Route component={About}/>
					</Switch>

					<ErrorSnackbar open={!!this.state.error} onClose={() => this.setState({ error: undefined })} error={this.state.error || ''}/>
				</MuiThemeProvider>
			</Router>
		);
	}
	
	private renderLoggedOutRoutes() {
		return (
			<>
				<Route path="/register" component={Register} />
				<Route path="/login" component={Login} />
			</>
		)
	}

	private renderLoggedOutMenu() {
		return <>
			<Link component={NavLink} href="/teams" color="inherit">Teams</Link>
			<Link component={NavLink} href="/login" color="inherit">Login</Link>
			<Link component={NavLink} href="/register" color="inherit">Register</Link>
		</>;
	}

	private renderLoggedInMenu() {
		return <>
			<Link component={NavLink} href={`/team/${authService.currentTwitterHandle}`} color="inherit">My Team</Link>
			<Link component={NavLink} href="/teams" color="inherit">Teams</Link>
			<Link component={NavLink} onClick={() => authService.logout()} color="inherit">Logout</Link>
		</>;
	}
}