import { Post } from "./post.model";
import { User } from "./user.model";

export interface Reaction{
    
    id: number;
    type: string;
    post: Post;
    madeBy: User;

}