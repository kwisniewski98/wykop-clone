CREATE TABLE "users"(
    id serial primary key ,
    username text not null check ( length(username) > 4 ) ,
    password text not null ,
    role text not null check (role in ('Admin', 'User')),
    registration_date date not null
);
CREATE TABLE content (
    id serial primary key,
    text text not null ,
    image text,
    video text
);

CREATE TABLE sub_vykop(
    id serial primary key ,
    name text not null ,
    description text not null
);
CREATE TABLE admin_list(
    id serial primary key,
    sub_vykop integer not null ,
    user_id integer not null ,
    constraint fk_user foreign key (user_id) references users(id),
    constraint fk_vykop foreign key (sub_vykop) references sub_vykop(id)
);
CREATE TABLE sub_vykop_list(
   id serial primary key,
   sub_vykop integer not null ,
   user_id integer not null ,
   constraint fk_user foreign key (user_id) references users(id),
   constraint fk_vykop foreign key (sub_vykop) references sub_vykop(id)
);
CREATE TABLE post (
    id serial primary key ,
    author integer not null,
    votes integer not null ,
    content integer not null ,
    creation_date date not null,
    sub_vykop_id integer not null,
    constraint fk_content foreign key (content) references content(id),
    constraint fk_author foreign key (author) references users(id)
);
CREATE TABLE comment (
     id serial primary key ,
     post integer,
     text text,
     votes integer not null,
     foreign key (post) references post(id)
);
insert into users (username, password, registration_date) values ('admin', 'password', '01-01-2000');
insert into users (username, password, registration_date) values ('user1', 'otherPassword', '01-08-2010');
insert into users (username, password, registration_date) values ('user2', 'nextPassword', '12-21-2019');
insert into content (text, image, video) values ('Only text', null, null);
insert into content (text, image, video) values ('Text with img', 'https://marketingprzykawie.pl/wp-content/uploads/2019/08/pepe-the-frog_large.jpg', null);
insert into content (text, image, video) values ('Text with video', null,'https://www.youtube.com/embed/dQw4w9WgXcQ?autoplay=0&fs=1&iv_load_policy=3&showinfo=0&rel=0&cc_load_policy=0&start=0&end=0');
insert into sub_vykop  (name, description) values ('Some subvykop', 'description');
insert into admin_list (sub_vykop, user_id) values (1, 1);
insert into sub_vykop_list (sub_vykop, user_id) values (1, 2);
insert into sub_vykop_list (sub_vykop, user_id) values (1, 3);
insert into post (author, votes, content, creation_date, sub_vykop_id) values (1, 0, 1, '01-01-2020', 1);
insert into post (author, votes, content, creation_date, sub_vykop_id) values (2, 0, 2, '01-01-2020', 1);
insert into post (author, votes, content, creation_date, sub_vykop_id) values (3, 0, 3, '01-01-2020', 1);
insert into comment (post, text, votes) values (1, 'some text', 0);

