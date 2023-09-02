import { GroupAdmin } from "./groupAdmin";
import { GroupRequest } from "./groupRequest";
import { Post } from "./post.model";

export interface Group {
    id: number;
    name: string;
    description: string;
    creationDate: Date; 
    isSuspended: boolean;
    suspendedReason: string | null;
    contains: Post[];
    groupAdmins: GroupAdmin[];
    groupRequests: GroupRequest[];
}