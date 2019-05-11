import React from 'react';
import { AppBar, Link, Paper, Toolbar } from '@material-ui/core';
import GroupIcon from '@material-ui/icons/Group';
import { BrowserRouter as Router, Link as RouteLink, Route } from 'react-router-dom';
import { AuthProvider } from './Auth/AuthContext';
import { AuthService } from './Auth/AuthService';
import { TeamList } from './Team/TeamList';
import { TeamInterface } from './interfaces/Team.interface';

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
					<div>
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
							<Route path="/teams" render={this.renderTeamList} />
							<Route path="/team/:twitterHandle" component={Users} />
						</Paper>
					</div>
				</Router>
			</AuthProvider>
		);
	}

	private readonly renderTeamList = () => {
		const teams: TeamInterface[] = [
			{ id: 1, members: [
				{
					id: 0,
					name: 'robeeert',
					profilePicUrl: 'https://pbs.twimg.com/profile_images/1091708094214795265/lm8MLEb3_400x400.jpg',
					tokens: [],
					twitterHandle: 'r0b3333rt'
				},
				{
					id: 1,
					name: 'Manfred Bausch',
					profilePicUrl: 'https://pbs.twimg.com/profile_images/761559147892596736/7vBhzxZX_400x400.jpg',
					tokens: [],
					twitterHandle: 'ensoniq23'
				},
			], name: 'team 1' },
			{ id: 2, members: [], name: 'team 2' },
			{ id: 3, members: [], name: 'team 3' },
		];

		return <TeamList teams={teams} />
	}
}