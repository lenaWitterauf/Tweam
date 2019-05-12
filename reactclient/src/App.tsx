import { AppBar, Button, Link, MuiThemeProvider, Paper, Toolbar } from '@material-ui/core';
import GroupIcon from '@material-ui/icons/Group';
import React from 'react';
import { BrowserRouter as Router, Link as RouteLink, Route } from 'react-router-dom';
import { AuthProvider } from './Auth/AuthContext';
import { AuthService } from './Auth/AuthService';
import {User} from './User/User'
import { networkService } from './Network/NetworkService';
import { Register } from './Register/Register';
import { TeamList } from './Team/TeamList';
import { darkTheme } from './themes/dark';

function Index() {
  return <h2>Home</h2>;
}

function Users() {
	return <h2>Users</h2>;
}

const MyLink = (props: { href?: string }): React.ReactElement => <RouteLink to={props.href || 'foo'} {...props}/>

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
								<div style={{ flexGrow: 1 }}/>
								<Link component={MyLink} href="/teams" color="inherit">Teams</Link>
								<Link component={MyLink} href="/about" color="inherit">
									<GroupIcon/> About
								</Link>
							</Toolbar>
						</AppBar>

						<Paper>
							<Route path="/" exact component={Index} />
							<Route path="/teams" component={TeamList} />
							<Route path="/team/:twitterHandle" component={Users} />
							<Route path="/register"  component={Register} />
                            <Route path="/user/:twitterHandle" component={User}/>
						</Paper>
					</MuiThemeProvider>
				</Router>
			</AuthProvider>
		);
	}
}