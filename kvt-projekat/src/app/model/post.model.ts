import { OnInit } from "@angular/core";
import { User } from "./user.model";
import { Image } from "./image";
import { Reaction } from "./reaction";
import { Comments } from "./comment";
import { Group } from "./group";

export interface Post{
    
    id: number;
    content: string;
    creationDate: Date;
    postedBy: User;
    isLiked: boolean;
    isDisliked: boolean;
    isHearted: boolean;
    images: any;
    comments:Comments[];
    reactions:Reaction[];
    isDeleted: boolean;
    group:Group;

}
  