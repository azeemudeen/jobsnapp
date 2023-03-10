import { Picture } from './picture';
import { Resume } from './resume';
import { Role } from './role';
import { SkillsAndExperience } from './skills-experience';
import { Connection } from './connection';
import { Chat } from './chat';
import { Job } from './job';

export class User {
  id: number;
  name: string;
  surname: string;
  username: string;
  password: string;
  passwordConfirm: string;
  roles: Array<Role> = new Array<Role>();
  phoneNumber: string;
  profilePicture: Picture;
  resumeFile: Resume;
  city: string;
  currentJob: string;
  currentCompany: string;
  usersFollowing:  Array<Connection> = new Array<Connection>();
  userFollowedBy:  Array<Connection> = new Array<Connection>();
  numOfConnections: number;
  education: Array <SkillsAndExperience> = new Array <SkillsAndExperience>();
  workExperience: Array <SkillsAndExperience> = new Array <SkillsAndExperience>();
  skills: Array <SkillsAndExperience> = new Array <SkillsAndExperience>();
  website: string;
  twitter: string;
  github: string;
  chats: Array <Chat> = new Array <Chat>();
  userType: string;
  jobsCreated: Array<Job> = new Array <Job>();
}
