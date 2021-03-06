CREATE TABLE "users"
(
    id                serial primary key,
    username          text not null check ( length(username) > 4 ),
    password          text not null,
    email             text not null,
    avatar            text not null,
    role              text not null check (role in ('Admin', 'User')),
    registration_date date not null
);
CREATE TABLE content
(
    id    serial primary key,
    text  text not null,
    image text,
    video text
);

CREATE TABLE sub_vykop
(
    id          serial primary key,
    name        text not null,
    description text not null,
    banner      text not null,
    avatar      text not null
);
CREATE TABLE admin_list
(
    id        serial primary key,
    sub_vykop integer not null,
    user_id   integer not null,
    constraint fk_user foreign key (user_id) references users (id) on delete cascade ,
    constraint fk_vykop foreign key (sub_vykop) references sub_vykop (id) on delete cascade
);
CREATE TABLE sub_vykop_list
(
    id        serial primary key,
    sub_vykop integer not null,
    user_id   integer not null,
    constraint fk_user foreign key (user_id) references users (id) on delete  cascade ,
    constraint fk_vykop foreign key (sub_vykop) references sub_vykop (id) on delete cascade
);
CREATE TABLE post
(
    id            serial primary key,
    author        integer not null,
    votes         integer not null,
    content       integer not null,
    creation_date date    not null,
    title         text    not null,
    sub_vykopid  integer not null,
    constraint fk_content foreign key (content) references content (id) on delete cascade ,
    constraint fk_author foreign key (author) references users (id)
);
CREATE TABLE comment
(
    id    serial primary key,
    post  integer,
    text  text,
    votes integer not null,
    author integer not null ,
    foreign key (post) references post (id),
    foreign key (author) references user (id)

);
CREATE TABLE upvoted_comment
(
    id         serial primary key,
    user_id    integer not null,
    comment_id integer not null,
    constraint fk_user foreign key (user_id) references users (id) on delete cascade ,
    constraint fk_comment foreign key (comment_id) references comment (id) on delete  cascade
);
CREATE TABLE upvoted_post
(
    id      serial primary key,
    user_id integer not null,
    post_id integer not null,
    constraint fk_user foreign key (user_id) references users (id) on delete cascade,
    constraint fk_post foreign key (post_id) references post (id) on delete cascade
);
insert into users (username, password, role, registration_date, email,avatar)
values ('admin', 'password', 'admin', '01-01-2000', 'admin@test.com', 'http://dummyimage.com/230x198.png/cc0000/ffffff');
insert into users (username, password, role, registration_date, email, avatar)
values ('user1', 'otherPassword', 'user', '01-08-2010', 'user1@test.com', 'http://dummyimage.com/122x168.jpg/dddddd/000000' );
insert into users (username, password, role, registration_date, email, avatar)
values ('user2', 'nextPassword', 'user', '12-21-2019', 'user2@test.com', 'http://dummyimage.com/118x177.bmp/ff4444/ffffff');
insert into content (text, image, video)
values ('Only text', null, null);
insert into content (text, image, video)
values ('Text with img', 'https://marketingprzykawie.pl/wp-content/uploads/2019/08/pepe-the-frog_large.jpg', null);
insert into content (text, image, video)
values ('Text with video', null,
        'https://www.youtube.com/embed/dQw4w9WgXcQ?autoplay=0&fs=1&iv_load_policy=3&showinfo=0&rel=0&cc_load_policy=0&start=0&end=0');
insert into sub_vykop (name, description, banner, avatar)
values ('Some subvykop', 'description','http://clipart-library.com/newimages/clip-art-banner-7.png', 'http://dummyimage.com/175x144.jpg/ff4444/ffffff' );
insert into admin_list (sub_vykop, user_id)
values (1, 1);
insert into sub_vykop_list (sub_vykop, user_id)
values (1, 2);
insert into sub_vykop_list (sub_vykop, user_id)
values (1, 3);
insert into post (author, votes, content, creation_date, sub_vykopid, title)
values (1, 0, 1, '01-01-2020', 1, 'Test');
insert into post (author, votes, content, creation_date, sub_vykopid, title)
values (2, 0, 2, '01-01-2020', 1, 'Kolejny');
insert into post (author, votes, content, creation_date, sub_vykopid, title)
values (3, 0, 3, '01-01-2020', 1, 'Kolejny test');
insert into comment (post, text, votes, author)
values (1, 'some text', 0, 1);

