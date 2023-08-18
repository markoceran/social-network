import { OnInit } from "@angular/core";
import { User } from "./user.model";
import { Image } from "./image";

export interface Post{
    
    id: number;
    content: string;
    creationDate: Date;
    postedBy: User;
    isLiked: boolean;
    isDisliked: boolean;
    isHearted: boolean;
    images: any;

}
  