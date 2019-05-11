import React from 'react';
import { AppBar, Link, Paper, Toolbar } from '@material-ui/core';
import { BrowserRouter as Router, Link as RouteLink, Route } from 'react-router-dom';

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

function AppRouter() {
  return (
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
  );
}

export default AppRouter;