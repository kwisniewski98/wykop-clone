CREATE TABLE users
(
    id                serial primary key,
    username          text not null check ( length(username) > 4 ),
    password          text not null,
    email             text not null,
    avatar            text,
    role              text not null,
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
    constraint fk_user_admin_list foreign key (user_id) references users (id) on delete cascade ,
    constraint fk_vykop_admin_list foreign key (sub_vykop) references sub_vykop (id) on delete cascade
);
CREATE TABLE sub_vykop_list
(
    id        serial primary key,
    sub_vykop integer not null,
    user_id   integer not null,
    constraint fk_user_sub_vykop_list foreign key (user_id) references users (id) on delete  cascade ,
    constraint fk_vykop_sub_vykop_list foreign key (sub_vykop) references sub_vykop (id) on delete cascade
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
    constraint fk_content_post foreign key (content) references content (id) on delete cascade ,
    constraint fk_author_post foreign key (author) references users (id) on delete cascade
);
CREATE TABLE comment
(
    id    serial primary key,
    post  integer,
    text  text,
    votes integer not null,
    author integer not null ,
    constraint fk_post_comment foreign key (post) references post (id),
    constraint fk_user_comment foreign key (author) references users (id)

);
CREATE TABLE upvoted_comment
(
    id         serial primary key,
    user_id    integer not null,
    comment_id integer not null,
    constraint fk_user_upvoted_comment foreign key (user_id) references users (id) on delete cascade ,
    constraint fk_comment_upvoted_comment foreign key (comment_id) references comment (id) on delete  cascade
);
CREATE TABLE upvoted_post
(
    id      serial primary key,
    user_id integer not null,
    post_id integer not null,
    constraint fk_user_upvoted_post foreign key (user_id) references users (id) on delete cascade,
    constraint fk_post_upvoted_post foreign key (post_id) references post (id) on delete cascade
);