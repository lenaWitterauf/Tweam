export interface AuthListener {
	onAuthChange(loggedIn: boolean): void;
}

export class AuthService {
	private readonly authListeners: AuthListener[] = [];
	private _loggedIn = true;

	constructor() {
		// TODO read cookie and initialize with corresponding state
	}

	login(): void {
		this._loggedIn = true;
		this.authListeners.forEach(listener => listener.onAuthChange(true));
	}
	
	logout(): void {
		this._loggedIn = false;
		this.authListeners.forEach(listener => listener.onAuthChange(false));
	}

	get loggedIn() { return this._loggedIn; }

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
