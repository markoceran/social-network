import { Group } from "./group";
import { User } from "./user.model";

export interface GroupAdmin{
    
    id: number;
    user: User;
    group: Group;

}