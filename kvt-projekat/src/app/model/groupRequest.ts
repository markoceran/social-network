import { Group } from "./group";
import { User } from "./user.model";

export interface GroupRequest {
    id: number;
    approved: boolean;
    createdAt: Date;
    at: Date;
    forr: Group;
    from: User;
  }
  