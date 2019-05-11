import { UserInterface } from "./User.interface";

export interface TeamInterface {
	id: number;
	name?: string;
	members: UserInterface[]
}
