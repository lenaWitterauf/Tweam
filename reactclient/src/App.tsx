import { AppBar, Link, MuiThemeProvider, Paper, Toolbar } from '@material-ui/core';
import GroupIcon from '@material-ui/icons/Group';
import React from 'react';
import { BrowserRouter as Router, Link as RouteLink, Route } from 'react-router-dom';
import { AuthProvider } from './Auth/AuthContext';
import { AuthService } from './Auth/AuthService';
import { Register } from './Register/Register';
import { TeamList } from './Team/TeamList';
import { darkTheme } from './themes/dark';
import { UserFetcher } from './User/UserFetcher';

const MyLink = (props: { href?: string }): React.ReactElement => <RouteLink to={props.href || ''} {...props} />

export class App extends React.PureComponent<{}, { isLoggedIn: boolean }> {
	private readonly authService = new AuthService();

	state = { isLoggedIn: false };

	componentDidMount() {
		this.authService.registerListener(this);
	}

	componentWillUnmount() {
		this.authService.unregisterListener(this);
	}

	readonly onAuthChange = (isLoggedIn: boolean) => {
		this.setState({ isLoggedIn });
	};

	render() {
		return (
			<AuthProvider value={{ loggedIn: this.state.isLoggedIn }}>
				<Router>
					<MuiThemeProvider theme={darkTheme}>
						<AppBar position="relative">
							<Toolbar>
								<Link variant="h5" component={MyLink} href="/home" color="inherit">Tweam</Link>
								<div style={{ flexGrow: 1 }} />
								<Link component={MyLink} href="/teams" color="inherit">Teams</Link>
								<Link component={MyLink} href="/about" color="inherit">
									<GroupIcon /> About
								</Link>
							</Toolbar>
						</AppBar>

						<Paper style={{background: '#777'}}>
							<Route path="/teams" component={TeamList} />
							<Route path="/register" component={Register} />
							<Route path="/user/:twitterHandle" component={UserFetcher} />
						</Paper>
					</MuiThemeProvider>
				</Router>
			</AuthProvider>
		);
	}
}