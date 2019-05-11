import { ServerUser } from "./ServerUser.interface";

export interface ServerTeam {
	id: number;
	teamPeople: ServerUser[];
	imageUrl: string;
}