import { TeamInterface } from "../interfaces/Team.interface";
import { UserInterface } from "../interfaces/User.interface";
import { ServerTeam } from "./ServerTeam.interface";

interface User {
	twitterHandle: string;
	tokens: string[];
}

const baseUrl = 'http://local.knuddels.de:8080'

class NetworkService {
	async createUser(user: User): Promise<UserInterface | undefined> {
		const response = await this.post('/user/create', {
			handle: user.twitterHandle,
			keywords: user.tokens,
		});

		if (!response || !response.ok) {
			return undefined;
		}

		const createdUser = await response.json();
		return {
			id: createdUser.id,
			name: createdUser.userName,
			twitterHandle: createdUser.twitterHandle,
			tokens: createdUser.userKeywords,
			profilePicUrl: '',
		};
	}

	async getUserByHandle(twitterHandle: string): Promise<UserInterface | undefined> {
		return this.get(`/user/${twitterHandle}`) as any;
	}

	async getTeamForUser(user: User): Promise<TeamInterface | undefined> {
		const response = await this.get(`/team/${user.twitterHandle}`);

		if (!response || !response.ok) {
			return undefined;
		}

		const team: ServerTeam = await response.json();
		return {
			id: team.id,
			for: {
				id: team.teamFor.id,
				name: team.teamFor.userName,
				twitterHandle: team.teamFor.twitterHandle,
				tokens: team.teamFor.userKeywords,
				profilePicUrl: team.teamFor.imageUrl,
			},
			members: team.teamPeople.map(user => ({
				id: user.id,
				name: user.userName,
				twitterHandle: user.twitterHandle,
				tokens: user.userKeywords,
				profilePicUrl: '',
			})),
		};
	}

	async getAllTeams(): Promise<TeamInterface[] | undefined> {
		const response: Response | undefined = await this.get('/team/all');
		
		if (!response || !response.ok) {
			return undefined;
		}

		const teams: ServerTeam[] = await response.json();
		return teams.map(team => ({
			id: team.id,
			for: {
				id: team.teamFor.id,
				name: team.teamFor.userName,
				twitterHandle: team.teamFor.twitterHandle,
				tokens: team.teamFor.userKeywords,
				profilePicUrl: '',
			},
			members: team.teamPeople.map(user => ({
				id: user.id,
				name: user.userName,
				twitterHandle: user.twitterHandle,
				tokens: user.userKeywords,
				profilePicUrl: '',
			})),
		}));
	}

	async updateMatches(): Promise<boolean> {
		const response = await this.put('/remap', {});
		
		if (!response || !response.ok) {
			return false;
		}

		return true;
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
			return fetch(`${baseUrl}${endpoint}`, {
				method,
				body,
				headers: {
					'Content-Type': 'application/json',
				},
			});
		} catch (e) {
			console.error(e);
			return undefined;
		}
	}
}

export const networkService = new NetworkService();