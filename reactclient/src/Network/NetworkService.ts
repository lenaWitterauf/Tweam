import { TeamInterface } from "../interfaces/Team.interface";
import { UserInterface } from "../interfaces/User.interface";
import { ServerTeam } from "./ServerTeam.interface";
import { ServerUser } from "./ServerUser.interface";
import { errorService } from "../ErrorService";

interface User {
	twitterHandle: string;
	tokens: string[];
}

const baseUrl = 'http://localhost:8080'

class NetworkService {
	async createUser(user: User): Promise<UserInterface | undefined> {
		const response = await this.post('/user/create', {
			handle: user.twitterHandle,
			keywords: user.tokens,
		});

		if (!response || !response.ok) {
			this.handleBusinessError(response);
			return undefined;
		}

		const createdUser: ServerUser = await response.json();
		return {
			id: createdUser.id,
			name: createdUser.userName,
			twitterHandle: createdUser.twitterHandle,
			tokens: createdUser.userKeywords.join(", "),
			profilePicUrl: createdUser.imageUrl,
			statusId: createdUser.statusId,
		};
	}

	async getUserByHandle(twitterHandle: string): Promise<UserInterface | undefined> {
		const response = await this.get(`/user/${twitterHandle}`);

		if (!response || !response.ok) {
			this.handleBusinessError(response);
			return undefined;
		}

		const user: ServerUser = await response.json();
		return {
			id: user.id,
			name: user.userName,
			twitterHandle: user.twitterHandle,
			tokens: user.userKeywords.join(", "),
			profilePicUrl: user.imageUrl,
			statusId: user.statusId,
		};
	}

	async getTeamForUser(user: UserInterface): Promise<TeamInterface | undefined> {
		const response = await this.get(`/team/${user.twitterHandle}`);

		if (!response || !response.ok) {
			this.handleBusinessError(response);
			return undefined;
		}

		const team: ServerTeam = await response.json();
		return {
			id: team.id,
			members: team.teamPeople.map(user => ({
				id: user.id,
				name: user.userName,
				twitterHandle: user.twitterHandle,
				tokens: user.userKeywords.join(", "),
				profilePicUrl: user.imageUrl,
				statusId: user.statusId,
			})),
		};
	}

	async getAllTeams(): Promise<TeamInterface[] | undefined> {
		const response: Response | undefined = await this.get('/team/all');
		
		if (!response || !response.ok) {
			this.handleBusinessError(response);
			return undefined;
		}

		const teams: ServerTeam[] = await response.json();
		return teams.map(team => ({
			id: team.id,
			members: team.teamPeople.map(user => ({
				id: user.id,
				name: user.userName,
				twitterHandle: user.twitterHandle,
				profilePicUrl: user.imageUrl,
				tokens: user.userKeywords.join(", "),
				statusId: user.statusId,
			})),
		}));
	}

	async updateMatches(): Promise<boolean> {
		const response = await this.post('/remap', {});
		
		if (!response || !response.ok) {
			this.handleBusinessError(response);
			return false;
		}

		return true;
	}

	private async handleBusinessError(response: Response | undefined) {
		if (!response) {
			errorService.error('Unknown network error. Please try again!');
			return;
		}

		if (!response.ok) {
			const serverError = await response.text();
			errorService.error(serverError);
			return;
		}

		errorService.error('Unknown error. Please contact our team.');
	}

	private async get(endpoint: string): Promise<Response | undefined> {
		return this.fetch(endpoint, 'get');
	}

	private async post(endpoint: string, payload: object): Promise<Response | undefined> {
		return this.fetch(endpoint, 'post', payload);
	}

	private async put(endpoint: string, payload: object): Promise<Response | undefined> {
		return this.fetch(endpoint, 'put', payload);
	}

	private async fetch(endpoint: string, method: string, payload?: object): Promise<Response | undefined> {
		let body = undefined;

		if (method !== 'get' && typeof payload !== 'undefined') {
			body = JSON.stringify(payload);
		}

		try {
			return await fetch(`${baseUrl}${endpoint}`, {
				method,
				body,
				headers: {
					'Content-Type': 'application/json',
				},
			});
		} catch (e) {
			console.error(e);
			errorService.error('Request failed. Please try again!')
			return undefined;
		}
	}
}

export const networkService = new NetworkService();