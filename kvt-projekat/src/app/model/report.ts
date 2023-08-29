import { ReportReason } from "./ReportReason";
import { Comments } from "./comment";
import { Post } from "./post.model";
import { User } from "./user.model";

export interface Report {
    
    id: number;
    reason: ReportReason;
    timestamp: Date; 
    byUser: User;
    accepted: boolean;
    post: Post;
    comment: Comments;
    user: User;
  
}