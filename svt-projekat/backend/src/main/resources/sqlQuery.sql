INSERT INTO `user`
VALUES('1','john@gmail.com','John','2023-05-19 10:00:00', 'Doe','123','USER','johnDoe'),
      ('2','jane@gmail.com', 'Jane','2023-05-19 11:30:00','Smith','456','USER','janeSmith');


INSERT INTO `post`
VALUES ('1','Post content 1', '2023-05-18 10:30:00', '1');

INSERT INTO `post`
VALUES ('2','Post content 2', '2023-05-19 12:45:00', '2');

INSERT INTO `groupp`
VALUES ('1','2023-05-19 15:45:00','Group 2 Description',  true ,'test' ,'Suspended due to inactivity');

INSERT INTO `groupp`
VALUES ('2','2023-06-26 18:25:00','Group 2 Description',  false ,'test' ,'');

use drustvenamreza;
select*from user;
alter table user drop column user_type;
select * from group_admin;
select*from friend_request;
select*from my_seqv3;
select*from post;
select*from administrator;
select*from image;
select*from friends_of_user;
select * from groupp;
select * from reaction;
select*from comment;
select * from report;
select*from banned;
select*from replies_of_comment;
select * from post_of_group;
select*from group_request;
alter table user drop key UK_ob8kqyqqgmefl0aco34akdtpe;
drop table admin_of_group;


INSERT INTO `groupp`
VALUES ('1','2023-05-19 15:45:00','Group 1 Description',  true ,'test' ,'Suspended due to inactivity');

INSERT INTO `groupp`
VALUES ('2','2023-06-26 18:25:00','Group 2 Description',  false ,'test' ,'');

INSERT INTO `post`
VALUES ('1','Post content 1', '2023-05-18 10:30:00', '2');

INSERT INTO `post`
VALUES ('2','Post content 2', '2023-05-19 12:45:00', '2');

