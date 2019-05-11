interface User {
	twitterHandle: string;
	tokens: string[];
}

const baseUrl = 'http://local.knuddels.de:8080'

export class NetworkService {
	async createUser(user: User) {
		return this.post('/user/create', {
			handle: user.twitterHandle,
			keywords: user.tokens,
		});
	}

	async getUserByHandle(twitterHandle: string) {
		return this.get(`/user/${twitterHandle}`);
	}

	async getTeamForUser(user: User) {
		return this.get(`/team/${user.twitterHandle}`);
	}

	async getAllTeams() {
		return this.get('/team/all');
	}

	async updateMatches() {
		
	}

	private async get(endpoint: string) {
		return this.fetch(endpoint, 'get');
	}

	private async post(endpoint: string, payload: object) {
		return this.fetch(endpoint, 'post', payload);
	}

	private async put(endpoint: string, payload: object) {
		return this.fetch(endpoint, 'put', payload);
	}

	private async fetch(endpoint: string, method: string, payload?: object) {
		let body = undefined;

		if (method !== 'get' && typeof payload !== 'undefined') {
			body = JSON.stringify(payload);
		}

		try {
			const response = fetch(`${baseUrl}${endpoint}`, {
				method,
				body,
				headers: {
					'Content-Type': 'application/json',
				},
			});
		} catch (e) {
			return new Promise((resolve, reject) => resolve(e));
		}
	}
}