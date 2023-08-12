import { User } from "./user.model";

export interface FriendRequest {

    id: number;
    approved: boolean;
    createdAt: Date;
    at: Date;
    from: User;
    forr: User;

}