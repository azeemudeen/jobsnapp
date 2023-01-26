import { User } from "./user";

export class UserNotification{
    message: number;
    timestamp: Date;
    user: User;
    positive: boolean;
    title: String;
}