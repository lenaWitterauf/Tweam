type Listener = (error: string) => void;

class ErrorService {
	private readonly listeners: Listener[] = [];

	error(message: string): void {
		this.listeners.forEach(listener => listener(message));
	}

	registerListener(listener: Listener) {
		this.listeners.push(listener);
	}

	unregisterLstener(listener: Listener) {
		const index = this.listeners.indexOf(listener);
		if (index > -1) {
			this.listeners.splice(index, 1);
		}
	}
}

export const errorService = new ErrorService();