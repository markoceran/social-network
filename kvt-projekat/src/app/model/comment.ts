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
}