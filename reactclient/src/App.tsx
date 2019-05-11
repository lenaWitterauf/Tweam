import React from 'react';
import { AppBar, Link, Paper, Toolbar } from '@material-ui/core';
import { BrowserRouter as Router, Link as RouteLink, Route } from 'react-router-dom';
import { AuthProvider } from './Auth/AuthContext';
import { AuthService } from './AuthService';

function Index() {
  return <h2>Home</h2>;
}

function About() {
	return <h2>About</h2>;
}

function Users() {
	return <h2>Users</h2>;
}

const MyLink = (props: { href?: string }): React.ReactElement => <RouteLink to={props.href || 'foo'} {...props}/>

class AppRouterBase extends React.PureComponent<undefined, { isLoggedIn: boolean }> {
	private readonly authService = new AuthService();
	
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
					<div>
						<AppBar position="relative">
							<Toolbar>
								<Link variant="h5" component={MyLink} href="/home" color="inherit">Tweam</Link>
								<div style={{ flexGrow: 1 }}/>
								<Link component={MyLink} href="/about" color="inherit">About</Link>
							</Toolbar>
						</AppBar>

						<Paper>
							<Route path="/" exact component={Index} />
							<Route path="/login" component={About} />
							<Route path="/users/" component={Users} />
						</Paper>
					</div>
				</Router>
			</AuthProvider>
		);
	}
}
