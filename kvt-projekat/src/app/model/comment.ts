import { Reaction } from "./reaction";
import { User } from "./user.model";

export interface Comments{

    id: number;
    text: string;
    timestamp: Date;
    belongsTo: User;
    replies: Comments[];
    isLiked: boolean;
    isDisliked: boolean;
    isHearted: boolean;
    reactions:Reaction[];
    isDeleted: boolean;
}