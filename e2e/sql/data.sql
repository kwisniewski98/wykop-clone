
insert into users (username, password, role, registration_date, email,avatar)
values ('admin', 'password', 'admin',to_date( '01-01-2000' , 'dd-mm-yyyy' ) , 'admin@test.com', 'http://dummyimage.com/230x198.png/cc0000/ffffff');
insert into users (username, password, role, registration_date, email, avatar)
values ('user1', 'otherPassword', 'user',to_date( '01-08-2010' , 'dd-mm-yyyy' ) , 'user1@test.com', 'http://dummyimage.com/122x168.jpg/dddddd/000000' );
insert into users (username, password, role, registration_date, email, avatar)
values ('user2', 'nextPassword', 'user', to_date( '12-21-2019' , 'dd-mm-yyyy' ), 'user2@test.com', 'http://dummyimage.com/118x177.bmp/ff4444/ffffff');
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
values (1, 0, 1,to_date( '01-01-2020' , 'dd-mm-yyyy' ), 1, 'Test');
insert into post (author, votes, content, creation_date, sub_vykopid, title)
values (2, 0, 2, to_date( '01-01-2020' , 'dd-mm-yyyy' ), 1, 'Kolejny');
insert into post (author, votes, content, creation_date, sub_vykopid, title)
values (3, 0, 3, to_date( '01-01-2020' , 'dd-mm-yyyy' ), 1, 'Kolejny test');
insert into comment (post, text, votes, author)
values (1, 'some text', 0, 1);

