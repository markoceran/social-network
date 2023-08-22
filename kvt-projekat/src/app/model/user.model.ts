export interface User {
	id: number;
	username: string;
	password: string;
	email: string;
	lastLogin: string; 
	firstName: string;
	lastName: string;
	roles: string;
	friendsWith: User[]; 
	profileImage: any;
	displayName:string;
	description:string;
}

  