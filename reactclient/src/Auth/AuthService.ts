interface AuthListener {
	onAuthChange(loggedIn: boolean): void;
}

class AuthService {
	private readonly authListeners: AuthListener[] = [];
	private twitterHandle = '';

	constructor() {
		// TODO read cookie and initialize with corresponding state
	}

	login(twitterHandle: string): void {
		this.twitterHandle = twitterHandle;
		this.authListeners.forEach(listener => listener.onAuthChange(true));
	}
	
	logout(): void {
		this.twitterHandle = '';
		this.authListeners.forEach(listener => listener.onAuthChange(false));
	}

	get loggedIn() { return !!this.twitterHandle; }

	get currentTwitterHandle() { return this.twitterHandle; }

	registerListener(authListener: AuthListener): void {
		this.authListeners.push(authListener);
	}

	unregisterListener(authListener: AuthListener): void {
		const indexOfListener = this.authListeners.indexOf(authListener);
		if (indexOfListener > -1) {
			this.authListeners.splice(indexOfListener, 1);
		}
	}
}

export const authService = new AuthService();